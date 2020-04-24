package com.derandecker.popularmoviesstage2.utils;

import com.derandecker.popularmoviesstage2.model.MovieEntry;
import com.derandecker.popularmoviesstage2.model.RelatedVideos;
import com.derandecker.popularmoviesstage2.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Review> parseReviewsJson(String json) throws JSONException {
        JSONObject jsonString = new JSONObject(json);
        JSONArray results = jsonString.getJSONArray("results");

        ArrayList<Review> reviews = new ArrayList<Review>();
        Review currentReviews;

        for (int i = 0; i < results.length(); i++) {
            JSONObject reviewItem = results.getJSONObject(i);
            String author = reviewItem.getString("author");
            String content = reviewItem.getString("content");

            currentReviews = new Review(author, content);
            reviews.add(currentReviews);
        }
        return reviews;


    }
}

