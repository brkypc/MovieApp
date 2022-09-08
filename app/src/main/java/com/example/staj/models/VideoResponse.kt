package com.example.staj.models

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results")
    val videos: List<Video>
)
