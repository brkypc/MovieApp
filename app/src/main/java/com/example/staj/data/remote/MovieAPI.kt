package com.example.staj.data.remote

import com.example.staj.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("movie/{movieID}")
    suspend fun getMovie(
        @Path("movieID") movieID: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") append: String
    ): MovieResponse?
}
