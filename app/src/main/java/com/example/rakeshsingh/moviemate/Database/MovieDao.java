package com.example.rakeshsingh.moviemate.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM tableFavoriteMovies ORDER BY id")
    LiveData<List<FavoriteMovie>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteMovie favMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovie favMovie);

    @Delete
    void deleteMovie(FavoriteMovie favMovie);

    @Query("SELECT * FROM tableFavoriteMovies WHERE id = :id")
    FavoriteMovie loadMovieById(int id);
}
