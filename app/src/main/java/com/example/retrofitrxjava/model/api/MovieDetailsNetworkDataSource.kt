package com.example.retrofitrxjava.model.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.retrofitrxjava.model.response.MovieDetailResponse
import com.example.retrofitrxjava.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(
    private val movieApiService: MovieApiService,
    private val compositeDisposable: CompositeDisposable
) {
    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetailResponse>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetailResponse>
        get() = _downloadedMovieDetailsResponse

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                movieApiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MDNetworkDataSource", it.message!!)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MDNetworkDataSource", e.message!!)
        }
    }
}