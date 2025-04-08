package com.example.jammp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class News extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> newsItemList = new ArrayList<>();
    private LinearLayout fullNewsLayout;
    private ImageView fullArticleImage, backArrow, newsBackArrow;
    private TextView fullArticleTitle, fullArticleContent;
    private LinearProgressIndicator progressIndicator;
    private String apiUrl = "https://gnews.io/api/v4/top-headlines?country=in&category=health&apikey=d3ee5f55f916203528da271efad7ba86";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        // Initialize views
        newsRecyclerView = findViewById(R.id.news_recycler_view);
        fullNewsLayout = findViewById(R.id.full_news_layout);
        fullArticleImage = findViewById(R.id.full_article_image);
        fullArticleTitle = findViewById(R.id.full_article_title);
        fullArticleContent = findViewById(R.id.full_article_content);
        backArrow = findViewById(R.id.back_arrow);
        newsBackArrow = findViewById(R.id.news_back_arrow);
        progressIndicator = findViewById(R.id.progress_bar);

        // Set up RecyclerView
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(newsItemList, this::onNewsItemClick);
        newsRecyclerView.setAdapter(newsAdapter);

        // Fetch news from the API
        fetchNews();

        // Back arrow actions
        backArrow.setOnClickListener(v -> showNewsList());
        newsBackArrow.setOnClickListener(v -> {
            Intent intent = new Intent(News.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up SearchView filtering
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false); // Make it always expanded
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newsAdapter.getFilter().filter(newText);
                return true;
            }
        });

        // Set up SearchView to show the keyboard on click
        searchView.setOnClickListener(v -> searchView.setIconified(false));
    }

    private void fetchNews() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        progressIndicator.setVisibility(View.VISIBLE);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    progressIndicator.setVisibility(View.GONE);
                    Toast.makeText(News.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray articles = jsonResponse.getJSONArray("articles");

                        // Clear previous items and fetch all available news items
                        newsItemList.clear();
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);
                            String title = article.getString("title");
                            String description = article.getString("description");
                            String imageUrl = article.getString("image");
                            String content = article.optString("content", "Content not available"); // Fallback for content
                            String source = article.getJSONObject("source").getString("name");

                            // Concatenate the content from the article if it's truncated
                            content = content.replaceAll("\\[.*?\\]", "");  // Remove any truncated indication like [2095 chars]
                            String url = article.getString("url");
                            content += "\n\nRead more at: " + url;  // Add the article's URL at the end for full reading.

                            newsItemList.add(new NewsItem(title, description, imageUrl, content, source));
                        }

                        runOnUiThread(() -> {
                            newsAdapter.notifyDataSetChanged(); // Notify adapter of data change
                            newsAdapter.getFilter().filter(" "); // Trigger filtering with a single space
                            newsRecyclerView.setVisibility(View.VISIBLE); // Make sure RecyclerView is visible
                            progressIndicator.setVisibility(View.GONE); // Hide progress indicator
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressIndicator.setVisibility(View.GONE);
                            Toast.makeText(News.this, "Error parsing news", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }

    private void onNewsItemClick(NewsItem newsItem) {
        fullNewsLayout.setVisibility(View.VISIBLE);
        newsRecyclerView.setVisibility(View.GONE);
        fullArticleTitle.setText(newsItem.getTitle());
        fullArticleContent.setText(newsItem.getContent());
        // Enable clickable links and set LinkMovementMethod to handle link clicks
        Linkify.addLinks(fullArticleContent, Linkify.WEB_URLS);
        fullArticleContent.setMovementMethod(LinkMovementMethod.getInstance());
        Picasso.get().load(newsItem.getImageUrl()).into(fullArticleImage);
    }

    private void showNewsList() {
        fullNewsLayout.setVisibility(View.GONE);
        newsRecyclerView.setVisibility(View.VISIBLE);
    }

    // NewsAdapter class for RecyclerView
    static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {
        private List<NewsItem> newsList;
        private List<NewsItem> filteredNewsList;
        private final OnItemClickListener onItemClickListener;

        interface OnItemClickListener {
            void onItemClick(NewsItem newsItem);
        }

        NewsAdapter(List<NewsItem> newsList, OnItemClickListener onItemClickListener) {
            this.newsList = newsList;
            this.filteredNewsList = new ArrayList<>(newsList);
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item_layout, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            NewsItem newsItem = filteredNewsList.get(position);
            holder.titleTextView.setText(newsItem.getTitle());
            holder.sourceTextView.setText(newsItem.getSource());
            Picasso.get().load(newsItem.getImageUrl()).into(holder.imageView);

            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(newsItem));
        }

        @Override
        public int getItemCount() {
            return filteredNewsList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String query = constraint.toString().toLowerCase();
                    List<NewsItem> filteredList = new ArrayList<>();

                    if (query.isEmpty()) {
                        filteredList.addAll(newsList);
                    } else {
                        for (NewsItem item : newsList) {
                            if (item.getTitle().toLowerCase().contains(query) ||
                                    item.getSource().toLowerCase().contains(query)) {
                                filteredList.add(item);
                            }
                        }
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredList;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredNewsList.clear();
                    filteredNewsList.addAll((List<NewsItem>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

        class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView, sourceTextView;
            ImageView imageView;

            NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.article_title);
                sourceTextView = itemView.findViewById(R.id.article_source);
                imageView = itemView.findViewById(R.id.article_image_view);
            }
        }
    }

    // NewsItem class to store news article information
    static class NewsItem {
        private final String title;
        private final String description;
        private final String imageUrl;
        private final String content;
        private final String source;

        NewsItem(String title, String description, String imageUrl, String content, String source) {
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.content = content;
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getContent() {
            return content;
        }

        public String getSource() {
            return source;
        }
    }
}
