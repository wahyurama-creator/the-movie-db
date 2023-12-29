package com.ways.themoviedb.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.ways.themoviedb.R
import com.ways.themoviedb.data.paging.utils.LoadingStateAdapter
import com.ways.themoviedb.data.remote.response.genre.Genre
import com.ways.themoviedb.data.utils.LoaderState
import com.ways.themoviedb.databinding.ActivityMainBinding
import com.ways.themoviedb.presentation.detail.DetailActivity
import com.ways.themoviedb.presentation.main.adapter.GenreAdapter
import com.ways.themoviedb.presentation.main.adapter.MoviePagingAdapter
import com.ways.themoviedb.presentation.main.viewModel.MainViewModel
import com.ways.themoviedb.presentation.utils.InternetConnection
import com.ways.themoviedb.presentation.utils.RecyclerViewDecorator
import com.ways.themoviedb.presentation.utils.calculateNumberOfGridColumn
import com.ways.themoviedb.presentation.utils.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    private val columnCount by lazy { calculateNumberOfGridColumn(180) }
    private lateinit var movieAdapter: MoviePagingAdapter
    private val genreAdapter by lazy { GenreAdapter() }
    private lateinit var loadingStateAdapter: LoadingStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initObservers()
        setupRecyclerView()
    }

    private fun initObservers() {
        lifecycle.addObserver(viewModel)
        viewModel.genres.observe(this) { genres ->
            setupGenre(genres)
        }
        viewModel.loadingState.observe(this) { loadState ->
            setupLoadingState(loadState)
        }
        viewModel.error.observe(this) { error ->
            error?.let { setupError(it) }
        }
        viewModel.selectedGenre.observe(this) { genre ->
            fetchMovies(genre)
        }
    }

    private fun setupGenre(genres: List<Genre>) {
        with(binding.rvGenre) {
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            genreAdapter.setData(genres)
            adapter = genreAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)

            genreAdapter.setOnContentClickListener { genre ->
                if (InternetConnection.hasInternetConnection(application)) {
                    viewModel.setSelectedGenre(genre)
                }
            }

            if (onFlingListener == null) {
                LinearSnapHelper().attachToRecyclerView(this)
            }
        }
    }

    private fun setupLoadingState(state: LoaderState) {
        with(binding) {
            when (state) {
                is LoaderState.ShowLoading -> {
                    loadingView.isVisible = true
                    window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }

                is LoaderState.HideLoading -> {
                    loadingView.isVisible = false
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }
    }

    private fun setupError(error: String) {
        showBottomSheet(
            title = getString(R.string.text_something_wrong_happen_title),
            description = error,
            layoutInflater = layoutInflater,
        )
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

        addOnLoadState()

        movieAdapter.setOnContentClickListener {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.KEY_INTENT_DETAIL_MOVIE, it.id)
            })
        }
    }

    private fun fetchMovies(genre: Genre) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            viewModel.getMovies(genre.id.toString()).collect {
                movieAdapter.submitData(it)
            }
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
                        when (it.error) {
                            is UnknownHostException -> viewModel.setError("No Internet Connection")
                            else -> viewModel.setError(it.error.message.toString())
                        }
                    }
                }
            }
        }
    }
}