package com.example.staj.data.local

import androidx.room.TypeConverter
import com.example.staj.models.Cast
import com.example.staj.models.Crew
import com.example.staj.models.Genre
import com.example.staj.models.Review
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
