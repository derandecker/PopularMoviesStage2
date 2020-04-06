package com.derandecker.popularmoviesstage2.utils;

import android.graphics.Movie;

import com.derandecker.popularmoviesstage2.model.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static List<MovieEntry> parseMovieJson(String json, Boolean popular, Boolean highestRated) throws JSONException {

        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        ArrayList<MovieEntry> movies = new ArrayList<MovieEntry>();
        MovieEntry currentMovie;
        Boolean fave = false;
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieDetail = results.getJSONObject(i);
            String title = movieDetail.getString("title");
            String imagePath = movieDetail.getString("poster_path");
            String overview = movieDetail.getString("overview");
            int voteAverage = movieDetail.getInt("vote_average");
            String releaseDate = movieDetail.getString("release_date");

            currentMovie = new MovieEntry(title, imagePath, overview, voteAverage,
                    releaseDate, popular, highestRated, fave);
            movies.add(currentMovie);
        }
        return movies;
    }
}

