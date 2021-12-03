package com.example.retrofitrxjava.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.retrofitrxjava.model.response.PopularMovieResponse.Movie
import com.example.retrofitrxjava.repository.MovieRepository
import com.example.retrofitrxjava.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieListViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable
        )
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.fetchNetworkStateMovieList()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}