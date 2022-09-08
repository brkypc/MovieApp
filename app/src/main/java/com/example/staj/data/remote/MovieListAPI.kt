package com.example.staj.data.remote

import com.example.staj.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieListAPI {

    @GET("trending/movie/week")
    suspend fun listMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieListResponse?
}
