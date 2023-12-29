package com.ways.themoviedb.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ways.themoviedb.BuildConfig
import com.ways.themoviedb.R
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.data.utils.LoaderState
import com.ways.themoviedb.databinding.ActivityDetailBinding
import com.ways.themoviedb.presentation.detail.adapter.ReviewAdapter
import com.ways.themoviedb.presentation.detail.adapter.VideoAdapter
import com.ways.themoviedb.presentation.detail.viewModel.DetailViewModel
import com.ways.themoviedb.presentation.utils.showBottomSheet
import com.ways.themoviedb.presentation.videoPlayer.VideoPlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>()

    private val reviewAdapter by lazy { ReviewAdapter() }
    private val videoAdapter by lazy { VideoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initObserver()
    }

    private fun initObserver() {
        viewModel.errorState.observe(this) {
            setupError(it)
        }
        viewModel.loadingState.observe(this) {
            setupLoadingView(it)
        }
        viewModel.movieDetail.observe(this) {
            setupViews(it)
        }
        viewModel.movieReviews.observe(this) {
            setupReview(it)
        }
        viewModel.movieVideos.observe(this) {
            setupVideo(it)
        }
    }

    private fun setupError(error: String) {
        showBottomSheet(
            title = getString(R.string.text_something_wrong_happen_title),
            description = error,
            layoutInflater = layoutInflater,
        )
    }

    private fun setupLoadingView(loadState: LoaderState) {
        with(binding) {
            loadingView.isVisible = loadState is LoaderState.ShowLoading
        }
    }

    private fun setupViews(data: MovieDetailResponse) {
        with(binding) {
            fabBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            Glide.with(applicationContext)
                .load(BuildConfig.BASE_IMG_URL + data.backdropPath)
                .placeholder(R.color.background_soft)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBackground)

            Glide.with(applicationContext)
                .load(BuildConfig.BASE_IMG_URL + data.posterPath)
                .placeholder(R.color.background_soft)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPoster)

            tvTitle.text = data.title
            tvReleaseDate.text = data.releaseDate
            tvRating.text = data.voteAverage.roundToInt().toString()

            tvStoryLine.text = data.overview
        }
    }

    private fun setupReview(data: List<ReviewResponse>) {
        with(binding) {
            if (data.isEmpty()) {
                tvLabelReview.isVisible = false
                rvReview.isVisible = false
                return
            }

            val linearLayoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

            rvReview.adapter = reviewAdapter
            rvReview.layoutManager = linearLayoutManager
            reviewAdapter.submitList(data)
        }
    }

    private fun setupVideo(data: List<VideoResponse>) {
        with(binding) {
            if (data.isEmpty()) {
                tvLabelVideo.isVisible = false
                rvVideo.isVisible = false
                return
            }

            val linearLayoutManager =
                LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)

            rvVideo.adapter = videoAdapter
            rvVideo.layoutManager = linearLayoutManager
            videoAdapter.submitList(data)

            videoAdapter.setOnContentClickListener {
                startActivity(Intent(this@DetailActivity, VideoPlayerActivity::class.java).apply {
                    putExtra(VideoPlayerActivity.KEY_INTENT_VIDEO_PLAYER_ID, it.key)
                })
            }
        }
    }

    companion object {
        const val KEY_INTENT_DETAIL_MOVIE = "key_intent_detail_movie"
    }
}