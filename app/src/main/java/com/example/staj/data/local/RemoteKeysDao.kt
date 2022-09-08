package com.example.staj.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.staj.models.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): RemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}