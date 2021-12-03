package com.example.retrofitrxjava.model.response.PopularMovieResponse


import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)