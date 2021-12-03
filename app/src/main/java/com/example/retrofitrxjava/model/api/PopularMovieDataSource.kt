package com.example.retrofitrxjava.model.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.retrofitrxjava.model.response.PopularMovieResponse.Movie
import com.example.retrofitrxjava.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularMovieDataSource(
    private val movieApiService: MovieApiService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            movieApiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.movies, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MDNetworkDataSource", it.message!!)
                })

        )
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            movieApiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.totalPages >= params.key) {
                        callback.onResult(it.movies, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.END_OF_LIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("MDNetworkDataSource", it.message!!)
                })

        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }
}