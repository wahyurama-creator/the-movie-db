package com.ways.themoviedb.data.paging.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ways.themoviedb.R
import com.ways.themoviedb.databinding.LayoutFooterPagingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingViewHolder>() {

    inner class LoadingViewHolder(
        parent: ViewGroup,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_footer_paging, parent, false)
    ) {
        private val binding = LayoutFooterPagingBinding.bind(itemView)
        private val loadingView = binding.loadingView
        private val errorMsg = binding.tvErrorMessage
        private val retry = binding.btnRetry
            .also {
                it.setOnClickListener { retry() }
            }

        fun bind(loadState: LoadState) {
            loadingView.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(
        holder: LoadingStateAdapter.LoadingViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateAdapter.LoadingViewHolder = LoadingViewHolder(parent, retry)

}