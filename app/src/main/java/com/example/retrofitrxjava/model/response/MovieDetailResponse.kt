package com.example.retrofitrxjava.model.response


import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val budget: Int,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val title: String,
    val tagline: String,
    @SerializedName("vote_average")
    val rating: Double
)