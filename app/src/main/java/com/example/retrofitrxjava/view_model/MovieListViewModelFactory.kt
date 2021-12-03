package com.example.retrofitrxjava.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitrxjava.repository.MovieRepository

class MovieListViewModelFactory (private val movieRepository: MovieRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieRepository) as T
    }
}