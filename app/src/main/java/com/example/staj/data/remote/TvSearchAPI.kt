package com.example.staj.data.remote

import com.example.staj.models.TVListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TvSearchAPI {

    @GET("search/tv")
    suspend fun listTV(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): TVListResponse?
}
