package com.example.retrofitrxjava.model.api

import com.example.retrofitrxjava.model.response.MovieDetailResponse
import com.example.retrofitrxjava.model.response.PopularMovieResponse.PopularMovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.themoviedb.org/3/movie/580489?api_key=c3d3c994ac0ed91217e9c13ff8f91dcd
//https://api.themoviedb.org/3/movie/popular?api_key=c3d3c994ac0ed91217e9c13ff8f91dcd&page=1
//https://api.themoviedb.org/3/

interface MovieApiService {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetailResponse>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<PopularMovieResponse>
}