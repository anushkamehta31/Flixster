package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    // Save the API URL as a constant
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    // Create tag to easily log data
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    // Instance variable to be use for binding class references
    private ActivityMainBinding binding;

    public static final String KEY_ITEM_TITLE = "movie_title";
    public static final String KEY_ITEM_SUMMARY = "movie_summary";
    public static final String KEY_ITEM_POSITION = "movie_position";
    public static final String KEY_ITEM_RATING = "movie_rating";
    public static final String KEY_ITEM_POSTER = "poster_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Use view binding to generate an instance variable that contains references to all views
        // (activity_main.xml -> ActivityMainBindind)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Create view variable for layout of activity
        // (layout of activity is stored in a special property called the root)
        View view = binding.getRoot();
        // Set the content view
        setContentView(view);
        // Get the recycler view reference variable through the binding variable
        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();

        MovieAdapter.OnClickListener onClickListener = new MovieAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // Check that we are getting inside this method properly
                Log.d("MainActivity", "Single click on poster at position " + position);
                // Create new activity using an intent
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                // Pass relevant data being viewed
                i.putExtra(KEY_ITEM_TITLE, movies.get(position).getTitle());
                i.putExtra(KEY_ITEM_SUMMARY, movies.get(position).getOverview());
                i.putExtra(KEY_ITEM_POSITION, position);
                i.putExtra(KEY_ITEM_RATING, movies.get(position).getRating());
                i.putExtra(KEY_ITEM_POSTER, movies.get(position).getPosterPath());
                // Display activity
                startActivity(i);
            }
        };

        /* Bind the adapter to the data source to populate the RecyclerView */
        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies, onClickListener);
        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        // Set a Layout Manager on the Recycler View
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Create an instance of the asynchronous http client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"onSuccess");
                // Retrieve the data that is contained in the JSON object
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results:" + results.toString());
                    // Get the array of movie objects
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    // Verify we are getting sensible models
                    Log.i(TAG, "Movies:" + movies.size());
                } catch (JSONException e) {
                    // Log error if we catch the exception
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"onFailure");
            }
        });
    }
}