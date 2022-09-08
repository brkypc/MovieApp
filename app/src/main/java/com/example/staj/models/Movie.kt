package com.example.staj.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey var movieID: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "favourite") var isFavourite: Boolean,
    @ColumnInfo(name = "popularity") var popularity: Double,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "voteAverage") var voteAverage: Double,
    @ColumnInfo(name = "voteCount") var voteCount: Int,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "posterPath") val posterPath: String?,
    @ColumnInfo(name = "backdropPath") val backdropPath: String?,
    @ColumnInfo(name = "budget") var budget: Int,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "revenue") var revenue: Int,
    @ColumnInfo(name = "runtime") var runtime: Int?,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "tagline") var tagline: String?,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "cast") var cast: List<Cast>?,
    @ColumnInfo(name = "crew") var crew: List<Crew>?,
    @ColumnInfo(name = "genres") var genres: List<Genre>,
    @ColumnInfo(name = "reviews") var reviews: List<Review>?
)
