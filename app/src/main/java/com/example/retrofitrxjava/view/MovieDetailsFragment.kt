package com.example.retrofitrxjava.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.retrofitrxjava.R
import com.example.retrofitrxjava.databinding.FragmentMovieDetailsBinding
import com.example.retrofitrxjava.model.api.MovieApiService
import com.example.retrofitrxjava.model.api.MovieClient
import com.example.retrofitrxjava.model.api.POSTER_BASE_URL
import com.example.retrofitrxjava.model.response.MovieDetailResponse
import com.example.retrofitrxjava.repository.MovieRepository
import com.example.retrofitrxjava.repository.NetworkState
import com.example.retrofitrxjava.view_model.MovieDetailsViewModel
import com.example.retrofitrxjava.view_model.MovieDetailsViewModelFactory
import java.text.NumberFormat
import java.util.*

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private var _viewBinding: FragmentMovieDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var viewModelFactory: MovieDetailsViewModelFactory
    private lateinit var repository: MovieRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        val movieId = args.movieId
        val apiService: MovieApiService = MovieClient.getClient()
        repository = MovieRepository(apiService)
        viewModelFactory = MovieDetailsViewModelFactory(repository, movieId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailsViewModel::class.java)
        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            bindMoviePoster(it)
            bindUI(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            viewBinding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            viewBinding.waiting.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    private fun bindMoviePoster(it: MovieDetailResponse) {
        val posterURl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(posterURl)
            .into(viewBinding.moviePoster)
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI(it: MovieDetailResponse) {
        val formatCurrency: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        viewBinding.budgetValue.text = "Budget: " + formatCurrency.format(it.budget)
        viewBinding.movieTitle.text = it.title.toString()
        viewBinding.movieTagline.text = it.tagline.toString()
        viewBinding.overviewContent.text = it.overview.toString()
        viewBinding.releaseData.text = "Release data: " + it.releaseDate.toString()
        viewBinding.revenueValue.text = "Revenue: " + formatCurrency.format(it.revenue)
        viewBinding.runtimeValue.text = "Runtime: " + it.runtime.toString() + " minutes"
        viewBinding.ratingValue.text = "Rating: " + it.rating.toString()

        viewBinding.movieInfo.text = "Movie Info:"
        viewBinding.overviewTitle.text = "Overview:"
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}
