package com.example.staj.data.local

import androidx.room.TypeConverter
import com.example.staj.models.Cast
import com.example.staj.models.Crew
import com.example.staj.models.Genre
import com.example.staj.models.MovieData
import com.example.staj.models.Review
import com.example.staj.models.TVData
import com.example.staj.models.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCastList(castList: List<Cast>?): String? {
        return if (castList == null) null
        else return Gson().toJson(castList, object : TypeToken<List<Cast>>() {}.type)
    }

    @TypeConverter
    fun toCastList(castString: String?): List<Cast>? {
        return if (castString == null) null
        else Gson().fromJson<List<Cast>>(castString, object : TypeToken<List<Cast>>() {}.type)
    }

    @TypeConverter
    fun fromCrewList(crewList: List<Crew>?): String? {
        return if (crewList == null) null
        else return Gson().toJson(crewList, object : TypeToken<List<Crew>>() {}.type)
    }

    @TypeConverter
    fun toCrewList(crewString: String?): List<Crew>? {
        return if (crewString == null) null
        else Gson().fromJson<List<Crew>>(crewString, object : TypeToken<List<Crew>>() {}.type)
    }

    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? {
        return if (genres == null) null
        else return Gson().toJson(genres, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre>? {
        return if (genresString == null) null
        else Gson().fromJson<List<Genre>>(genresString, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun fromReviewList(reviews: List<Review>?): String? {
        return if (reviews == null) null
        else return Gson().toJson(reviews, object : TypeToken<List<Review>>() {}.type)
    }

    @TypeConverter
    fun toReviewList(reviewsString: String?): List<Review>? {
        return if (reviewsString == null) null
        else Gson().fromJson<List<Review>>(reviewsString, object : TypeToken<List<Review>>() {}.type)
    }
}

class ConvertersData {
    @TypeConverter
    fun fromMovieDataList(movies: List<MovieData>?): String? {
        return if (movies == null) null
        else return Gson().toJson(movies, object : TypeToken<List<MovieData>>() {}.type)
    }

    @TypeConverter
    fun toMovieDataList(moviesString: String?): List<MovieData>? {
        return if (moviesString == null) null
        else Gson().fromJson<List<MovieData>>(moviesString, object : TypeToken<List<MovieData>>() {}.type)
    }

    @TypeConverter
    fun fromTvDataList(tvList: List<TVData>?): String? {
        return if (tvList == null) null
        else return Gson().toJson(tvList, object : TypeToken<List<TVData>>() {}.type)
    }

    @TypeConverter
    fun toTvDataList(tvListString: String?): List<TVData>? {
        return if (tvListString == null) null
        else Gson().fromJson<List<TVData>>(tvListString, object : TypeToken<List<TVData>>() {}.type)
    }

    @TypeConverter
    fun fromVideosList(videos: List<Video>?): String? {
        return if (videos == null) null
        else return Gson().toJson(videos, object : TypeToken<List<Video>>() {}.type)
    }

    @TypeConverter
    fun toVideosList(videosString: String?): List<Video>? {
        return if (videosString == null) null
        else Gson().fromJson<List<Video>>(videosString, object : TypeToken<List<Video>>() {}.type)
    }
}
