package com.example.staj.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.staj.models.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getAllMoviesPaging(): PagingSource<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesPaging(movies: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteAllPaging()

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT 20 OFFSET :limit")
    suspend fun getAllMovies(limit: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE favourite = 1 ORDER BY popularity DESC")
    fun getFavouriteMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE favourite = 1 ORDER BY popularity DESC")
    fun getFavourites(): List<Movie>

    @Query("SELECT * FROM movie WHERE movieID = :movieID")
    suspend fun getMovie(movieID: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Query(
        "UPDATE movie SET " +
            "popularity = :popularity, voteAverage = :voteAverage, voteCount =  :voteCount " +
            "WHERE movieID = :movieID"
    )
    fun updateMovie(movieID: Int, popularity: Double, voteAverage: Double, voteCount: Int)
}
