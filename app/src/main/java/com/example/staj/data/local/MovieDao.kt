package com.example.staj.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.staj.models.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT 20 OFFSET :offset")
    suspend fun getAllMovies(offset: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE favourite = 1 ORDER BY popularity DESC")
    fun getFavouriteMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE movieID = :movieID")
    suspend fun getMovie(movieID: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Query("UPDATE movie SET popularity = :p, voteAverage = :vA, voteCount =  :vC WHERE movieID = :id")
    fun updateMovie(id: Int, p: Double, vA: Double, vC: Int)
}
