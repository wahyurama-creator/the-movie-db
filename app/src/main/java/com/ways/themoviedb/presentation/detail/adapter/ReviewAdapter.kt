package com.ways.themoviedb.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.databinding.ItemMovieReviewBinding


class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewResponse>() {
            override fun areItemsTheSame(
                oldItem: ReviewResponse,
                newItem: ReviewResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ReviewResponse,
                newItem: ReviewResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    private val differ: AsyncListDiffer<ReviewResponse> = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        return ViewHolder(
            ItemMovieReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemMovieReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReviewResponse) {
            with(binding) {
                tvAuthor.text = data.author
                tvContent.text = data.content
            }
        }
    }

    fun submitList(reviews: List<ReviewResponse>) {
        differ.submitList(reviews)
    }
}