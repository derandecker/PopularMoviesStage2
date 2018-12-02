package com.derandecker.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {


    private MovieImageAdapter mAdapter;
    private RecyclerView mMoviesPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPics = (RecyclerView) findViewById(R.id.rv_movies);

        int spanSize = 3;

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, spanSize);

        mMoviesPics.setLayoutManager(gridLayoutManager);

        mMoviesPics.setHasFixedSize(true);
        mAdapter = new MovieImageAdapter(30);
        mMoviesPics.setAdapter(mAdapter);

    }
}
