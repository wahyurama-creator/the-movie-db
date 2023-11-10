package com.ways.themoviedb.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ways.themoviedb.BuildConfig
import com.ways.themoviedb.R
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.databinding.ItemMovieVideoBinding


class VideoAdapter : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoResponse>() {
            override fun areItemsTheSame(
                oldItem: VideoResponse,
                newItem: VideoResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: VideoResponse,
                newItem: VideoResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private val differ: AsyncListDiffer<VideoResponse> = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.ViewHolder {
        return ViewHolder(
            ItemMovieVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemMovieVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VideoResponse) {
            with(binding) {
                val thumbnail =
                    BuildConfig.BASE_YOUTUBE_THUMBNAIL_URL + data.key + BuildConfig.BASE_YOUTUBE_THUMBNAIL_URL_ENDPOINT
                Picasso.with(root.context)
                    .load(thumbnail)
                    .placeholder(R.color.grey_dark)
                    .into(ivPoster)

                ivPlay.setOnClickListener { onContentClickListener?.let { it1 -> it1(data) } }
            }
        }
    }

    fun submitList(reviews: List<VideoResponse>) {
        differ.submitList(reviews)
    }

    private var onContentClickListener: ((VideoResponse) -> Unit)? = null

    fun setOnContentClickListener(listener: (VideoResponse) -> Unit) {
        onContentClickListener = listener
    }
}