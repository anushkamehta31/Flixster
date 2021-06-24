package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityDetailsBinding;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;
    RatingBar ratingBar;

    // Use view binding to create an instance of binding class
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_details);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Grab references to each of the views using binding reference variable
        tvTitle = binding.title2;
        tvOverview = binding.overview;
        ivPoster = binding.ivPoster2;
        ratingBar = binding.rating;

        Toast.makeText(DetailsActivity.this, "View Details", Toast.LENGTH_SHORT).show();

        // Get handle on the data we passed in from Main Activity
        // Place poster into ivPoster
        Glide.with(this).load(getIntent().getStringExtra(MainActivity.KEY_ITEM_POSTER)).fitCenter()
                .transform(new RoundedCornersTransformation(30,10)).into(ivPoster);
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