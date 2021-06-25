package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// A Java object/model that will encapsulate the idea of a movie
public class Movie {

    // Instance variables for essential data corresponding to the movie
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    double rating;
    Integer id;

    // Constructor takes in JSON object and constructs movie object
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }

    // Method returns to us a list of movies from the JSON array that we received back
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        // Iterate through JSON array and construct a movie for each element in JSON array
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    // Want to get data out of these objects using getters
    public String getPosterPath() {
        /*
        You can also make a request to the configurations API, appending that to the base URl, and then
        appending that to the relative path that you get from the movie now playing API. What we do here is paste the
        URL and hardcode the size we want.
         */
        // Below, %s is replaced with the posterPath variable (string)
        // We also hardcoded size to be 342
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTrailerPath() {
        return String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=64a8bf5b6a5eb9411876b4e84e69391e", id);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }
}
