package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // We need to define an interface that will communicate back to Main Activity and tell us which position was tapped
    public interface OnClickListener {
        void onItemClicked(int position);
    }

    // Need a context to inflate a view
    Context context;
    List<Movie> movies;
    OnClickListener clickListener;


    public MovieAdapter(Context context, List<Movie> movies, OnClickListener clickListener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = clickListener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        // Pass in a context and inflates item_movie.xml
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        // Wrap return value inside of a viewholder
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter","onBindViewHolder" + position);
        // Get the movie at the position in the list
        Movie movie = movies.get(position);
        // Bind the movie data into the viewholder
        holder.bind(movie);
    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ViewHolder is a representation of a row/item in the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Member variables for each view in the view holder
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get references to each component (we can't use view binding for MovieAdapter class)
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            int placeholder;
            // We want to attach a click listener onto our Image View so that if it is clicked
            // we can open the new activity.
            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When Android notifies us that the poster was clicked, we invoked the
                    // method on our interface and pass in the position.
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            // if phone is in landscape then imageURL = backdrop image else poster image
            imageURL = context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE ? movie.getBackdropPath() : movie.getPosterPath();

            // if phone is in landscape then get landscape placeholder, otherwise use portrait placeholder
            placeholder = context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;

            // Load the imade using Glide and the correct url and placeholder.
            // Additionally, use .transform() and .fitCenter() to round the corners
            Glide.with(context).load(imageURL).fitCenter()
                    .transform(new RoundedCornersTransformation(30,10))
                    .placeholder(placeholder)
                    .into(ivPoster);

        }
    }
}
