package com.ways.themoviedb.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ways.themoviedb.data.paging.utils.LoadingStateAdapter
import com.ways.themoviedb.databinding.ActivityMainBinding
import com.ways.themoviedb.presentation.detail.DetailActivity
import com.ways.themoviedb.presentation.main.adapter.MoviePagingAdapter
import com.ways.themoviedb.presentation.main.viewModel.MainViewModel
import com.ways.themoviedb.presentation.utils.RecyclerViewDecorator
import com.ways.themoviedb.presentation.utils.calculateNumberOfGridColumn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    private val columnCount by lazy { calculateNumberOfGridColumn(180) }
    private lateinit var movieAdapter: MoviePagingAdapter
    private lateinit var loadingStateAdapter: LoadingStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, columnCount)
        movieAdapter = MoviePagingAdapter()
        loadingStateAdapter = LoadingStateAdapter(movieAdapter::retry)

        with(binding.rvMovie) {
            adapter = movieAdapter.withLoadStateFooter(loadingStateAdapter)
            layoutManager = gridLayoutManager
            gridLayoutManager.spanSizeLookup = gridSpanLookup
            addItemDecoration(RecyclerViewDecorator(columnCount, 10, true, context))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getMovies().collect {
                    movieAdapter.submitData(it)
                }
            }
        }

        addOnLoadState()

        movieAdapter.setOnContentClickListener {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.KEY_INTENT_DETAIL_MOVIE, it.id)
            })
        }
    }

    private val gridSpanLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (position == movieAdapter.itemCount && loadingStateAdapter.itemCount > 0) {
                columnCount
            } else {
                1
            }
        }
    }

    private fun addOnLoadState() {
        with(binding) {
            movieAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    loadingView.isVisible = true
                } else {
                    loadingView.isVisible = false

                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Timber.e("onError ${it.error}")
                    }
                }
            }
        }
    }
}