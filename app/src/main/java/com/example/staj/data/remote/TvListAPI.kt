package com.example.staj.data.remote

import com.example.staj.models.TVListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TvListAPI {

    @GET("discover/tv")
    suspend fun listTV(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int
    ): TVListResponse?
}
