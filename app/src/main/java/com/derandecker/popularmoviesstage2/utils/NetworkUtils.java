package com.derandecker.popularmoviesstage2.utils;

import androidx.lifecycle.LiveData;
import android.net.Uri;

import com.derandecker.popularmoviesstage2.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String API_KEY = BuildConfig.MY_API_KEY;

    final private static String WATCH_PATH = "watch";
    final private static String VIDEO_ID_PARAM = "v";
    final private static String API_KEY_PARAM = "api_key";
    final private static String VIDEOS_PATH = "videos";
    final private static String REVIEWS_PATH = "reviews";


    public static URL buildMoviesUrl(String MOVIE_URL) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildRelatedVideosUrl(String VIDEOS_URL, int movieId) {
        String movieIdString = Integer.toString(movieId);
        Uri builtUri = Uri.parse(VIDEOS_URL).buildUpon()
                .appendPath(movieIdString)
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewsUrl(String VIDEOS_URL, int movieId) {
        String movieIdString = Integer.toString(movieId);
        Uri builtUri = Uri.parse(VIDEOS_URL).buildUpon()
                .appendPath(movieIdString)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildYoutubeUri(String YOUTUBE_BASE_URL, String videoID){
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(WATCH_PATH)
                .appendQueryParameter(VIDEO_ID_PARAM, videoID)
                .build();
        return builtUri;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}