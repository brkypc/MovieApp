package com.example.staj.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.staj.models.TV

@Dao
interface TVDao {

    @Query("SELECT * FROM tv ORDER BY popularity DESC LIMIT :limit")
    suspend fun getAllTV(limit: Int): List<TV>

    @Query("SELECT * FROM tv WHERE favourite = 1 ORDER BY popularity DESC")
    fun getFavouriteTV(): LiveData<List<TV>>

    @Query("SELECT * FROM tv WHERE tvID = :tvID")
    suspend fun getTV(tvID: Int): TV?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tv: TV)

    @Update
    suspend fun update(tv: TV)

    @Query(
        "UPDATE tv SET " +
            "popularity = :popularity, voteAverage = :voteAverage, voteCount =  :voteCount " +
            "WHERE tvID = :tvID"
    )
    fun updateTv(tvID: Int, popularity: Double, voteAverage: Double, voteCount: Int)
}
