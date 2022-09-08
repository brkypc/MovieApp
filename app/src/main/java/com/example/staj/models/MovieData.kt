package com.example.staj.models

import com.example.staj.util.Constants.COUNT_THRESHOLD
import com.example.staj.util.Constants.COUNT_THRESHOLD_FLOAT
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class MovieData(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
) {
    companion object {
        fun toCountString(vote: Double, count: Int): String {
            var mCount = count

            if (count > COUNT_THRESHOLD) {
                mCount = (count / COUNT_THRESHOLD_FLOAT).roundToInt() * COUNT_THRESHOLD
            }

            return vote.toString().plus(" (")
                .plus(mCount).plus("+)")
        }
    }
}
