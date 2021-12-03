package com.example.retrofitrxjava.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.retrofitrxjava.model.api.MovieApiService
import com.example.retrofitrxjava.model.api.PopularMovieDataSource
import com.example.retrofitrxjava.model.response.PopularMovieResponse.Movie
import io.reactivex.disposables.CompositeDisposable


class PopularMovieDataSourceFactory(
    private val movieApiService: MovieApiService,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>(){

    val popularMovieLiveDataSource = MutableLiveData<PopularMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val popularMovieDataSource = PopularMovieDataSource(movieApiService, compositeDisposable)

        popularMovieLiveDataSource.postValue(popularMovieDataSource)
        return  popularMovieDataSource
    }
}