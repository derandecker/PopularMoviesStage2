package com.derandecker.popularmoviesstage2.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.graphics.Movie;

import com.derandecker.popularmoviesstage2.model.MovieEntry;

@Database(entities = {MovieEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemovies";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
