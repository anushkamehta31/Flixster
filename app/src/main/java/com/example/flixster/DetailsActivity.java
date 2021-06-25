package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;
    RatingBar ratingBar;
    ImageView backgroundImage;
    ImageButton playBtn;
    public static final String KEY_VIDEO_ID = "trailer_id";
    String videoID;

    // Use view binding to create an instance of binding class
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // Grab references to each of the views using binding reference variable
        tvTitle = binding.title2;
        tvOverview = binding.overview;
        ivPoster = binding.ivPoster2;
        ratingBar = binding.rating;
        backgroundImage = binding.backgroundImage;
        playBtn = binding.playBtn;

        Toast.makeText(DetailsActivity.this, "View Details", Toast.LENGTH_SHORT).show();

        // Get handle on the data we passed in from Main Activity
        // Place poster into ivPoster
        Glide.with(this).load(getIntent().getStringExtra(MainActivity.KEY_ITEM_POSTER)).fitCenter()
                .transform(new RoundedCornersTransformation(30,10)).into(ivPoster);
        // Load in background image
        Glide.with(this).load(getIntent().getStringExtra(MainActivity.KEY_BACKGROUND_PATH)).fitCenter()
                .transform(new RoundedCornersTransformation(30,10)).into(backgroundImage);
        // Set title and summary
        tvTitle.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TITLE));
        tvOverview.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_SUMMARY));
        // Set Rating
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize((float) 0.01);
        Float f = (float) getIntent().getExtras().getDouble(MainActivity.KEY_ITEM_RATING)/2;
        ratingBar.setRating((float) f);
        Log.d("Details", "Rating " + f);


        // Create an instance of the asynchronous http client to retrieve videos
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getIntent().getStringExtra(MainActivity.KEY_TRAILER_PATH), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // Retrieve the data that is contained in the JSON object
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Get the the first key in the results array
                    videoID = results.getJSONObject(0).getString("key");
                } catch (JSONException e) {
                    // Log error if we catch the exception
                    Log.e("DetailsActivity", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("DetailsActivity","onFailure");
            }
        });

        // Set on click listener to play the video
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to pass to MovieTrailerActivity
                Intent i = new Intent(DetailsActivity.this, MovieTrailerActivity.class);
                // Pass the videoID of the movie to MovieTrailerActivity
                i.putExtra(KEY_VIDEO_ID, videoID);
                // Toast to show that we are now playing the trailer
                Toast.makeText(DetailsActivity.this, "Playing Trailer", Toast.LENGTH_SHORT).show();
                // Begin the MovieTrailerActivity
                startActivity(i);
            }
        });

        // Return back to main when user is done reading
        ivPoster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Finish the activity
                finish();
            }
        });
    }
}