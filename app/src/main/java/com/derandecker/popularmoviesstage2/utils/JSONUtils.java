package com.derandecker.popularmoviesstage2.utils;

import android.arch.lifecycle.LiveData;

import com.derandecker.popularmoviesstage2.model.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONUtils {
    public static MovieEntry parseMovieJson(String json, int position) throws JSONException {
        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        JSONObject movieDetail = results.getJSONObject(position);
        int id = movieDetail.getInt("id");
        String title = movieDetail.getString("title");
        String imagePath = movieDetail.getString("poster_path");
        String overview = movieDetail.getString("overview");
        int voteAverage = movieDetail.getInt("vote_average");
        String releaseDate = movieDetail.getString("release_date");

        MovieEntry movie = new MovieEntry(id, title, imagePath, overview, voteAverage, releaseDate);
        return movie;
    }
}

