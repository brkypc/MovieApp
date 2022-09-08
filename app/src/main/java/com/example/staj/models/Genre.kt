package com.example.staj.models

import com.example.staj.util.Constants.ACTION
import com.example.staj.util.Constants.ACTION_ADVENTURE
import com.example.staj.util.Constants.ADVENTURE
import com.example.staj.util.Constants.ANIMATION
import com.example.staj.util.Constants.COMEDY
import com.example.staj.util.Constants.CRIME
import com.example.staj.util.Constants.DOCUMENTARY
import com.example.staj.util.Constants.DRAMA
import com.example.staj.util.Constants.FAMILY
import com.example.staj.util.Constants.FANTASY
import com.example.staj.util.Constants.HISTORY
import com.example.staj.util.Constants.HORROR
import com.example.staj.util.Constants.KIDS
import com.example.staj.util.Constants.MUSIC
import com.example.staj.util.Constants.MYSTERY
import com.example.staj.util.Constants.NEWS
import com.example.staj.util.Constants.REALITY
import com.example.staj.util.Constants.ROMANCE
import com.example.staj.util.Constants.SCI_FI
import com.example.staj.util.Constants.SCI_FI_FANTASY
import com.example.staj.util.Constants.SOAP
import com.example.staj.util.Constants.TALK
import com.example.staj.util.Constants.THRILLER
import com.example.staj.util.Constants.TV_MOVIE
import com.example.staj.util.Constants.WAR
import com.example.staj.util.Constants.WAR_POLITICS
import com.example.staj.util.Constants.WESTERN
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) {
    companion object {
        private val hashMapMovie = HashMap<Int, String>()
        private val hashMapTv = HashMap<Int, String>()

        init {
            createMovieHashMap()
            createTvHashMap()
        }

        fun toGenreListMovie(list: List<Int>): List<Genre> {
            val genreList = mutableListOf<Genre>()
            list.forEach {
                genreList.add(Genre(it, hashMapMovie[it] ?: "Genre"))
            }
            return genreList
        }

        fun toGenreListTV(list: List<Int>): List<Genre> {
            val genreList = mutableListOf<Genre>()
            list.forEach {
                genreList.add(Genre(it, hashMapTv[it] ?: "Genre"))
            }
            return genreList
        }

        fun toGenreString(genreList: List<Genre>): String {
            val genreString = StringBuilder()
            genreList.forEachIndexed { index, genre ->
                genreString.append(genre.name)
                if (index != genreList.lastIndex) {
                    genreString.append(", ")
                }
            }
            return genreString.toString()
        }

        private fun createTvHashMap() {
            hashMapTv[ACTION_ADVENTURE] = "Action & Adventure"
            hashMapTv[ANIMATION] = "Animation"
            hashMapTv[COMEDY] = "Comedy"
            hashMapTv[CRIME] = "Crime"
            hashMapTv[DOCUMENTARY] = "Documentary"
            hashMapTv[DRAMA] = "Drama"
            hashMapTv[FAMILY] = "Family"
            hashMapTv[KIDS] = "Kids"
            hashMapTv[MYSTERY] = "Mystery"
            hashMapTv[NEWS] = "News"
            hashMapTv[REALITY] = "Reality"
            hashMapTv[SCI_FI_FANTASY] = "Sci-Fi & Fantasy"
            hashMapTv[SOAP] = "Soap"
            hashMapTv[TALK] = "Talk"
            hashMapTv[WAR_POLITICS] = "War & Politics"
            hashMapTv[WESTERN] = "Western"
        }

        private fun createMovieHashMap() {
            hashMapMovie[ADVENTURE] = "Adventure"
            hashMapMovie[FANTASY] = "Fantasy"
            hashMapMovie[ANIMATION] = "Animation"
            hashMapMovie[DRAMA] = "Drama"
            hashMapMovie[HORROR] = "Horror"
            hashMapMovie[ACTION] = "Action"
            hashMapMovie[COMEDY] = "Comedy"
            hashMapMovie[HISTORY] = "History"
            hashMapMovie[WESTERN] = "Western"
            hashMapMovie[THRILLER] = "Thriller"
            hashMapMovie[CRIME] = "Crime"
            hashMapMovie[DOCUMENTARY] = "Documentary"
            hashMapMovie[SCI_FI] = "Science Fiction"
            hashMapMovie[MYSTERY] = "Mystery"
            hashMapMovie[MUSIC] = "Music"
            hashMapMovie[ROMANCE] = "Romance"
            hashMapMovie[FAMILY] = "Family"
            hashMapMovie[WAR] = "War"
            hashMapMovie[TV_MOVIE] = "TV Movie"
        }
    }
}
