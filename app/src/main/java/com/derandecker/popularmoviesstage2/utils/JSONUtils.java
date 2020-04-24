package com.derandecker.popularmoviesstage2.utils;

import android.util.Log;

import com.derandecker.popularmoviesstage2.MainActivity;
import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.model.RelatedVideos;
import com.derandecker.popularmoviesstage2.model.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class JSONUtils {

    public static List<MovieEntry> parseMovieJson(String json, final boolean popular, final boolean highestRated) throws JSONException {

        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        ArrayList<MovieEntry> movies = new ArrayList<MovieEntry>();
        MovieEntry currentMovie;
        Boolean fave = false;

        for (int i = 0; i < results.length(); i++) {
            JSONObject movieDetail = results.getJSONObject(i);
            int id = movieDetail.getInt("id");
            String title = movieDetail.getString("title");
            String imagePath = movieDetail.getString("poster_path");
            String overview = movieDetail.getString("overview");
            int voteAverage = movieDetail.getInt("vote_average");
            String releaseDate = movieDetail.getString("release_date");


            currentMovie = new MovieEntry(id, i, title, imagePath, overview, voteAverage,
                    releaseDate, popular, highestRated, fave);
            movies.add(currentMovie);
        }
        return movies;
    }

    public static List<RelatedVideos> parseRelatedVideoJson(String json) throws JSONException {
        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        ArrayList<RelatedVideos> relatedVideos = new ArrayList<RelatedVideos>();
        RelatedVideos currentRelatedVideos;

        for (int i = 0; i < results.length(); i++) {
            JSONObject relatedVideoItem = results.getJSONObject(i);
            String key = relatedVideoItem.getString("key");
            String name = relatedVideoItem.getString("name");

            currentRelatedVideos = new RelatedVideos(key, name);
            relatedVideos.add(currentRelatedVideos);
        }
        return relatedVideos;
    }

    public static List<Reviews> parseReviewsJson(String json) throws JSONException {
        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        Reviews currentReviews;

        for (int i = 0; i < results.length(); i++) {
            JSONObject reviewItem = results.getJSONObject(i);
            String author = reviewItem.getString("author");
            String content = reviewItem.getString("content");

            currentReviews = new Reviews(author, content);
            reviews.add(currentReviews);
        }
        return reviews;


    }
}

