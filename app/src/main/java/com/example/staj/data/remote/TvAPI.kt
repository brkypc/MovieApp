package com.example.staj.data.remote

import com.example.staj.models.TVResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvAPI {

    @GET("tv/{tvID}")
    suspend fun getTV(
        @Path("tvID") tvID: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") append: String
    ): TVResponse?
}
