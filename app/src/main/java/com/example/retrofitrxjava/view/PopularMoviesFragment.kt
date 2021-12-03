package com.example.retrofitrxjava.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitrxjava.R
import com.example.retrofitrxjava.databinding.FragmentPopularMoviesBinding
import com.example.retrofitrxjava.model.api.MovieApiService
import com.example.retrofitrxjava.model.api.MovieClient
import com.example.retrofitrxjava.repository.MovieRepository
import com.example.retrofitrxjava.repository.NetworkState
import com.example.retrofitrxjava.view_model.MovieDetailsViewModel

import com.example.retrofitrxjava.view_model.MovieListViewModel
import com.example.retrofitrxjava.view_model.MovieListViewModelFactory


class PopularMoviesFragment : Fragment(R.layout.fragment_popular_movies) {

    private var _viewBinding: FragmentPopularMoviesBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var viewModel: MovieListViewModel
    private lateinit var viewModelFactory: MovieListViewModelFactory
    private lateinit var repository: MovieRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPopularMoviesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService: MovieApiService = MovieClient.getClient()
        repository = MovieRepository(apiService)
        viewModelFactory = MovieListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)

        val movieAdapter = PopularMoviePagedListAdapter(context)

        val gridLayoutManager = GridLayoutManager(context, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }
        viewBinding.movieRv.let {
            it.layoutManager = gridLayoutManager
            it.adapter = movieAdapter
            it.setHasFixedSize(true)
        }
        viewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            viewBinding.progressBar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            viewBinding.errorTv.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null

    }
}