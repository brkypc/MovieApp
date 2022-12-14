package com.example.staj.extensions

import com.example.staj.models.Genre
import com.example.staj.models.Movie
import com.example.staj.models.MovieData
import com.example.staj.models.MovieResponse
import com.example.staj.models.TV
import com.example.staj.models.TVData
import com.example.staj.models.TVResponse

fun MovieResponse.toMovie() = Movie(
    movieID = id, isFavourite = false, title = title, popularity = popularity,
    releaseDate = releaseDate, voteAverage = voteAverage, voteCount = voteCount,
    overview = overview ?: "", posterPath = posterPath, backdropPath = backdropPath, budget = budget,
    language = originalLanguage, revenue = revenue, runtime = runtime, status = status, tagline = tagline,
    adult = adult, cast = credits.cast, crew = credits.crew, genres = genres, reviews = reviews.reviewList,
    similar = similar.results, videos = videos.videos
)

fun MovieData.toMovie() = Movie(
    movieID = id, isFavourite = false, title = title, popularity = popularity,
    releaseDate = releaseDate ?: "", voteAverage = voteAverage, voteCount = voteCount,
    overview = overview, posterPath = posterPath, backdropPath = backdropPath, budget = -1,
    language = originalLanguage, revenue = 0, runtime = 0, status = "", tagline = "",
    adult = adult, cast = null, crew = null, genres = Genre.toGenreListMovie(genreIds), reviews = null,
    similar = null, videos = null
)

fun TVResponse.toTV() = TV(
    tvID = id, isFavourite = false, name = name, popularity = popularity, firstAirDate = firstAirDate ?: "",
    lastAirDate = lastAirDate, voteAverage = voteAverage, voteCount = voteCount, overview = overview,
    posterPath = posterPath, backdropPath = backdropPath, language = originalLanguage,
    episodeRunTime = toRuntime(episodeRunTime), numberOfEpisodes = numberOfEpisodes,
    numberOfSeasons = numberOfSeasons, inProduction = inProduction, status = status, type = type,
    tagline = tagline, adult = adult, cast = credits.cast, crew = credits.crew,
    genres = genres, reviews = reviews.reviewList, similar = similar.results, videos = videos.videos
)

fun TVData.toTV() = TV(
    tvID = id, isFavourite = false, name = name, popularity = popularity, firstAirDate = firstAirDate ?: "",
    lastAirDate = null, voteAverage = voteAverage, voteCount = voteCount, overview = overview,
    posterPath = posterPath, backdropPath = backdropPath, language = originalLanguage,
    episodeRunTime = -1, numberOfEpisodes = null, numberOfSeasons = null,
    inProduction = false, status = null, type = null, tagline = null, adult = false,
    cast = null, crew = null, genres = Genre.toGenreListTV(genreIds), reviews = null,
    similar = null, videos = null
)

fun toRuntime(episodeRunTime: List<Int>): Int {
    return if (episodeRunTime.isNotEmpty()) {
        episodeRunTime[0]
    } else {
        0
    }
}
