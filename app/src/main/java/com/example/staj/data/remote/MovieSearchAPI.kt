package com.example.staj.data.remote

import com.example.staj.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieSearchAPI {

    @GET("search/movie")
    suspend fun listMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MovieListResponse?
}
