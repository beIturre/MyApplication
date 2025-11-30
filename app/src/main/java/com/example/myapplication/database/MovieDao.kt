package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones de películas
 */
@Dao
interface MovieDao {
    
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>
    
    @Query("SELECT * FROM movies")
    fun getAllMoviesFlow(): Flow<List<MovieEntity>>
    
    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): MovieEntity?
    
    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun getMovieByIdFlow(movieId: Int): Flow<MovieEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
}

