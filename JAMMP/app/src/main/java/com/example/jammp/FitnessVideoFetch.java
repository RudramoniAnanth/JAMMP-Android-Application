package com.example.jammp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

public class FitnessVideoFetch extends AppCompatActivity {
    private static final String YOUTUBE_API_KEY = "AIzaSyAud3Hf3nAyO-c9L-9mSTllIUJPYGVlKPQ";
    private static final String YOUTUBE_SEARCH_URL =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=50&q=fitness&key=" + YOUTUBE_API_KEY;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private ArrayList<String> videoTitlesList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_vid_fetch);

        ImageView backArrow = findViewById(R.id.back_arrow);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.video_titles_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoAdapter = new VideoAdapter(videoTitlesList);
        recyclerView.setAdapter(videoAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("FitnessVideos");

        // Back arrow functionality
        backArrow.setOnClickListener(v -> finish());

        fetchAndStoreVideos();
    }

    private void fetchAndStoreVideos() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            ArrayList<HashMap<String, String>> newVideos = new ArrayList<>();
            try {
                while (newVideos.size() < 100) {  // Continue fetching until we have 100 unique videos
                    URL url = new URL(YOUTUBE_SEARCH_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray items = response.getAsJsonArray("items");

                    for (int i = 0; i < items.size(); i++) {
                        JsonObject item = items.get(i).getAsJsonObject();
                        String videoId = item.getAsJsonObject("id").get("videoId").getAsString();
                        JsonObject snippet = item.getAsJsonObject("snippet");
                        String title = snippet.get("title").getAsString();
                        String thumbnailUrl = snippet.getAsJsonObject("thumbnails").getAsJsonObject("default").get("url").getAsString();
                        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                        // Check and add video if not already present
                        if (!checkAndAddVideoIfNotExists(videoId, title, thumbnailUrl, videoUrl, newVideos)) {
                            continue;
                        }

                        if (newVideos.size() >= 100) {
                            break;  // Stop fetching if we've reached the required number of new videos
                        }
                    }
                }

                // Store new videos in the database
                int serialNumber = 1;
                for (HashMap<String, String> videoData : newVideos) {
                    String videoId = videoData.get("videoUrl").split("v=")[1];
                    databaseReference.child(videoId).setValue(videoData);
                    videoTitlesList.add(serialNumber + ". " + videoData.get("title") + " - " + videoData.get("timestamp"));
                    serialNumber++;
                }

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    videoAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                Log.e("FitnessVideoFetch", "Error fetching data", e);
                runOnUiThread(() -> Toast.makeText(FitnessVideoFetch.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private boolean checkAndAddVideoIfNotExists(String videoId, String title, String thumbnailUrl, String videoUrl, ArrayList<HashMap<String, String>> newVideos) {
        final boolean[] exists = {false};
        CountDownLatch latch = new CountDownLatch(1);

        // Check Firebase for the video's existence
        databaseReference.child(videoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exists[0] = snapshot.exists();
                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FitnessVideoFetch", "Database error", error.toException());
                latch.countDown();
            }
        });

        try {
            latch.await();  // Wait for Firebase to complete the check
        } catch (InterruptedException e) {
            Log.e("FitnessVideoFetch", "Error checking video existence", e);
        }

        if (!exists[0]) {
            // If video does not exist, add it to newVideos
            HashMap<String, String> videoData = new HashMap<>();
            videoData.put("title", title);
            videoData.put("thumbnailUrl", thumbnailUrl);
            videoData.put("videoUrl", videoUrl);
            videoData.put("timestamp", getCurrentISTTimestamp());
            newVideos.add(videoData);
        }
        return !exists[0];
    }

    private String getCurrentISTTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return sdf.format(System.currentTimeMillis());
    }

    // Inner VideoAdapter class
    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
        private ArrayList<String> videoTitles;

        public VideoAdapter(ArrayList<String> videoTitles) {
            this.videoTitles = videoTitles;
        }

        @NonNull
        @Override
        public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
            holder.titleTextView.setText(videoTitles.get(position));
        }

        @Override
        public int getItemCount() {
            return videoTitles.size();
        }

        public class VideoViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
