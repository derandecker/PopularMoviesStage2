package com.derandecker.popularmoviesstage1.utils;

import com.derandecker.popularmoviesstage1.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONUtils {
    public static Movie parseMovieJson(String json, int position) throws JSONException {
        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        JSONObject movieDetail = results.getJSONObject(position);
        int id = movieDetail.getInt("id");
        String title = movieDetail.getString("title");
        String imagePath = movieDetail.getString("poster_path");
        String overview = movieDetail.getString("overview");
        int voteAverage = movieDetail.getInt("vote_average");
        String releaseDate = movieDetail.getString("release_date");

        Movie movie = new Movie(id, title, imagePath, overview, voteAverage, releaseDate);
        return movie;
    }
}

