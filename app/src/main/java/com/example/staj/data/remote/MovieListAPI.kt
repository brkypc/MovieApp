package com.example.staj.data.remote

import com.example.staj.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieListAPI {

    @GET("discover/movie")
    suspend fun listMovies(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int
    ): MovieListResponse
}
