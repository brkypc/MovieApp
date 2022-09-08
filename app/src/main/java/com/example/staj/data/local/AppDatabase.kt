package com.example.staj.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.staj.models.Movie
import com.example.staj.models.TV

@Database(entities = [Movie::class, TV::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class, ConvertersData::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MovieDao(): MovieDao
    abstract fun TVDao(): TVDao
}
