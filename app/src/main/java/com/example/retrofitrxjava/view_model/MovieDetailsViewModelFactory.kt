package com.example.retrofitrxjava.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitrxjava.repository.MovieRepository

class MovieDetailsViewModelFactory(private val movieRepository: MovieRepository, private val movieId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieRepository, movieId) as T
    }
}