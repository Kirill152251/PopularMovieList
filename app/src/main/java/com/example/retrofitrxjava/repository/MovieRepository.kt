package com.example.retrofitrxjava.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.ContiguousPagedList
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.retrofitrxjava.model.api.MovieApiService
import com.example.retrofitrxjava.model.api.MovieDetailsNetworkDataSource
import com.example.retrofitrxjava.model.api.POST_PER_PAGE
import com.example.retrofitrxjava.model.api.PopularMovieDataSource
import com.example.retrofitrxjava.model.response.MovieDetailResponse
import com.example.retrofitrxjava.model.response.PopularMovieResponse.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieRepository(private val movieApiService: MovieApiService) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var popularMovieDataSourceFactory: PopularMovieDataSourceFactory

    fun fetchSingleMovieDetails(
        movieId: Int,
        compositeDisposable: CompositeDisposable
    ): LiveData<MovieDetailResponse> {
        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(movieApiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun fetchNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>>{
        popularMovieDataSourceFactory = PopularMovieDataSourceFactory(movieApiService, compositeDisposable)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(popularMovieDataSourceFactory, config).build()

        return moviePagedList
    }

    fun fetchNetworkStateMovieList(): LiveData<NetworkState> {
        return Transformations.switchMap<PopularMovieDataSource, NetworkState>(
            popularMovieDataSourceFactory.popularMovieLiveDataSource, PopularMovieDataSource::networkState)
    }
}