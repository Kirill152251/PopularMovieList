package com.example.retrofitrxjava.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitrxjava.R
import com.example.retrofitrxjava.model.api.POSTER_BASE_URL
import com.example.retrofitrxjava.model.response.PopularMovieResponse.Movie
import com.example.retrofitrxjava.repository.NetworkState

class PopularMoviePagedListAdapter(public val context: Context?) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position),context!!)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }




    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val movieTitle = view.findViewById<TextView>(R.id.movie_title_rv)
        val movieReleaseData = view.findViewById<TextView>(R.id.movie_release_date_rv)
        val moviePoster = view.findViewById<ImageView>(R.id.movie_poster_rv)

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie?, context: Context) {
            movieTitle.text = movie?.title
            movieReleaseData.text = "Release data: " + movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(moviePoster);

            itemView.setOnClickListener{
                val movieId = movie!!.id
                it.findNavController().navigate(PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailsFragment(movieId))
            }

        }

    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val errorMsg = view.findViewById<TextView>(R.id.error_msg_rv)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_rv)

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                progressBar.visibility = View.VISIBLE;
            }
            else  {
                progressBar.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                errorMsg.visibility = View.VISIBLE;
                errorMsg.text = networkState.errorMsg;
            }
            else if (networkState != null && networkState == NetworkState.END_OF_LIST) {
                errorMsg.visibility = View.VISIBLE;
                errorMsg.text = networkState.errorMsg;
            }
            else {
                errorMsg.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}