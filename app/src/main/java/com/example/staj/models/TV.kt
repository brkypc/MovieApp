package com.example.staj.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TV(
    @PrimaryKey val tvID: Int,
    @ColumnInfo(name = "favourite") var isFavourite: Boolean,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "popularity") var popularity: Double,
    @ColumnInfo(name = "firstAirDate") val firstAirDate: String,
    @ColumnInfo(name = "lastAirDate") var lastAirDate: String?,
    @ColumnInfo(name = "voteAverage") var voteAverage: Double,
    @ColumnInfo(name = "voteCount") var voteCount: Int,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "posterPath") val posterPath: String?,
    @ColumnInfo(name = "backdropPath") val backdropPath: String?,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "episodeRunTime") var episodeRunTime: Int?,
    @ColumnInfo(name = "numberOfEpisodes") var numberOfEpisodes: Int?,
    @ColumnInfo(name = "numberOfSeasons") var numberOfSeasons: Int?,
    @ColumnInfo(name = "inProduction") var inProduction: Boolean,
    @ColumnInfo(name = "status") var status: String?,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "tagline") var tagline: String?,
    @ColumnInfo(name = "adult") var adult: Boolean,
    @ColumnInfo(name = "cast") var cast: List<Cast>?,
    @ColumnInfo(name = "crew") var crew: List<Crew>?,
    @ColumnInfo(name = "genres") var genres: List<Genre>,
    @ColumnInfo(name = "reviews") var reviews: List<Review>?,
    @ColumnInfo(name = "similar") var similar: List<TVData>?,
    @ColumnInfo(name = "videos") var videos: List<Video>?,
)
