package com.example.jammp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StayFit extends AppCompatActivity {
    private ImageView backArrow;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private ProgressBar linearProgressIndicator;
    private List<VideoItem> videoList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stay_fit);

        backArrow = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        linearProgressIndicator = findViewById(R.id.linear_progress_indicator);

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(StayFit.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(videoAdapter);

        // Load video metadata from the database
        loadVideoMetadata();

        // Set up SearchView to filter video list
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                videoAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                videoAdapter.filter(newText);
                return false;
            }
        });
    }

    private void loadVideoMetadata() {
        // Show the linear progress indicator while fetching data
        linearProgressIndicator.setVisibility(View.VISIBLE);

        // Reference to the "FitnessVideos" node in your Firebase database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("FitnessVideos");

        // Listen for changes in the "FitnessVideos" node
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot videoSnapshot : snapshot.getChildren()) {
                    // Fetch individual fields from the database
                    String thumbnailUrl = videoSnapshot.child("thumbnailUrl").getValue(String.class);
                    String videoUrl = videoSnapshot.child("videoUrl").getValue(String.class);
                    String title = videoSnapshot.child("title").getValue(String.class);

                    // Create a new VideoItem object and add it to the list
                    VideoItem videoItem = new VideoItem(thumbnailUrl, videoUrl, title);
                    videoList.add(videoItem);
                }

                // Hide the linear progress indicator and refresh the RecyclerView
                linearProgressIndicator.setVisibility(View.GONE);
                videoAdapter.notifyDataSetChanged();

                // Trigger the filter with a double space to display initial filtered list
                videoAdapter.filter(" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hide the progress indicator and log the error if data fetch fails
                linearProgressIndicator.setVisibility(View.GONE);
                Log.e("StayFit", "Error fetching data: " + error.getMessage());
            }
        });
    }

    // Inner class: VideoAdapter for RecyclerView
    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
        private List<VideoItem> videos;
        private List<VideoItem> videosFiltered;

        public VideoAdapter(List<VideoItem> videos) {
            this.videos = videos;
            this.videosFiltered = new ArrayList<>(videos);
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            VideoItem video = videosFiltered.get(position);
            holder.bind(video);
        }

        @Override
        public int getItemCount() {
            return videosFiltered.size();
        }

        public void filter(String query) {
            videosFiltered.clear();
            for (VideoItem video : videos) {
                if (video.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    videosFiltered.add(video);
                }
            }
            notifyDataSetChanged();
        }

        public class VideoViewHolder extends RecyclerView.ViewHolder {
            private PlayerView videoPlayerView;
            private TextView videoTitle;
            private ImageView thumbnailImage;
            private ExoPlayer exoPlayer;

            public VideoViewHolder(View itemView) {
                super(itemView);
                videoPlayerView = itemView.findViewById(R.id.video_player_view);
                videoTitle = itemView.findViewById(R.id.video_title);
                thumbnailImage = itemView.findViewById(R.id.video_thumbnail);

                // Initialize ExoPlayer
                exoPlayer = new ExoPlayer.Builder(itemView.getContext()).build();
                videoPlayerView.setPlayer(exoPlayer);
            }

            public void bind(VideoItem video) {
                videoTitle.setText(video.getTitle());

                // Load the thumbnail image using Glide
                Glide.with(thumbnailImage.getContext())
                        .load(video.getThumbnailUrl())
                        .into(thumbnailImage);

                // Set up click listener to play video on YouTube when thumbnail is clicked
                itemView.setOnClickListener(v -> {
                    String videoUrl =video.getVideoUrl(); // Replace with video.getVideoUrl() if the full YouTube URL is stored

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Check if YouTube app is available
                    PackageManager packageManager = itemView.getContext().getPackageManager();
                    List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                    if (resolveInfos.size() > 0) {
                        // Open the YouTube app if available
                        itemView.getContext().startActivity(intent);
                    } else {
                        // Fall back to web browser if YouTube app is unavailable
                        intent.setData(Uri.parse(video.getVideoUrl()));
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

        }


    }

    // Inner class: VideoItem to hold video data
    private class VideoItem {
        private String title;
        private String videoUrl;
        private String thumbnailUrl;

        public VideoItem(String thumbnailUrl, String videoUrl, String title) {
            this.thumbnailUrl = thumbnailUrl;
            this.videoUrl = videoUrl;
            this.title = title;
        }

        public String getTitle() { return title; }
        public String getVideoUrl() { return videoUrl; }
        public String getThumbnailUrl() { return thumbnailUrl; }
    }
}
