package com.ways.themoviedb.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ways.themoviedb.BuildConfig
import com.ways.themoviedb.R
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.databinding.ItemMovieGridDynamicBinding

class MoviePagingAdapter :
    PagingDataAdapter<MovieDetailResponse, MoviePagingAdapter.ViewHolder>(MovieComparator) {

    private object MovieComparator : DiffUtil.ItemCallback<MovieDetailResponse>() {
        override fun areItemsTheSame(
            oldItem: MovieDetailResponse, newItem: MovieDetailResponse
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieDetailResponse, newItem: MovieDetailResponse
        ): Boolean = oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieGridDynamicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val binding: ItemMovieGridDynamicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieDetailResponse) {
            with(binding) {
                tvTitle.text = data.title
                tvGenre.text = data.releaseDate
                tvRating.text = data.voteAverage.toString()

                Picasso.with(root.context)
                    .load(BuildConfig.BASE_IMG_URL + data.backdropPath)
                    .placeholder(R.color.background_soft)
                    .into(ivPoster)

                root.setOnClickListener { onContentClickListener?.let { it1 -> it1(data) } }
            }
        }
    }

    private var onContentClickListener: ((MovieDetailResponse) -> Unit)? = null

    fun setOnContentClickListener(listener: (MovieDetailResponse) -> Unit) {
        onContentClickListener = listener
    }

}