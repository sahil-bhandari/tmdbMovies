package com.sahil.roomexample.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sahil.roomexample.modelMovie.Result;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleMovie(Result result);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleMovies(List<Result> moviesList);

    @Query("SELECT * FROM Result WHERE Id = :movieId")
    Result fetchOneMoviesbyMovieId(int movieId);

    @Update
    void updateMovie(Result movies);

    @Delete
    void deleteMovie(Result movies);

    @Query("SELECT * FROM Result")
    List<Result> getAll();
}
