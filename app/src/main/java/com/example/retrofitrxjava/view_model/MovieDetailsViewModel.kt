package com.example.retrofitrxjava.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitrxjava.model.response.MovieDetailResponse
import com.example.retrofitrxjava.repository.MovieRepository
import com.example.retrofitrxjava.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    movieId: Int
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetailResponse> by lazy {
        movieRepository.fetchSingleMovieDetails(movieId, compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.fetchNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}