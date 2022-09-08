package com.example.staj.models

import com.google.gson.annotations.SerializedName

data class TVListResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TVData>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
