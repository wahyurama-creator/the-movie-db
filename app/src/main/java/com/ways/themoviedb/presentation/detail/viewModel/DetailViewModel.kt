package com.ways.themoviedb.presentation.detail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.data.remote.util.ResponseState
import com.ways.themoviedb.data.repository.MovieRepository
import com.ways.themoviedb.data.utils.LoaderState
import com.ways.themoviedb.presentation.detail.DetailActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    state: SavedStateHandle
) : ViewModel(), DetailViewModelContract {

    private val movieId: Int = state[DetailActivity.KEY_INTENT_DETAIL_MOVIE] ?: 0

    private var _loadingState = MutableLiveData<LoaderState>()
    val loadingState: LiveData<LoaderState> = _loadingState

    private var _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    private var _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail: LiveData<MovieDetailResponse> = _movieDetail

    private var _movieReviews = MutableLiveData<List<ReviewResponse>>()
    val movieReviews: LiveData<List<ReviewResponse>> = _movieReviews

    private var _movieVideos = MutableLiveData<List<VideoResponse>>()
    val movieVideos: LiveData<List<VideoResponse>> = _movieVideos

    init {
        getMovieDetail(movieId)
        getMovieReviews(movieId)
        getMovieVideos(movieId)
    }

    override fun getMovieDetail(movieId: Int) {
        _loadingState.value = LoaderState.ShowLoading
        viewModelScope.launch {
            movieRepository.getMovieDetail(movieId).collect {
                when (it) {
                    is ResponseState.Success -> {
                        _loadingState.value = LoaderState.HideLoading
                        _movieDetail.value = it.data?.body()
                    }

                    is ResponseState.Error -> {
                        _loadingState.value = LoaderState.HideLoading
                        _errorState.value = it.exception
                    }
                }
            }
        }
    }

    override fun getMovieReviews(movieId: Int) {
        _loadingState.value = LoaderState.ShowLoading
        viewModelScope.launch {
            movieRepository.getMovieReviews(movieId).collect {
                when (it) {
                    is ResponseState.Success -> {
                        _loadingState.value = LoaderState.HideLoading
                        _movieReviews.value = it.data?.body()?.results
                    }

                    is ResponseState.Error -> {
                        _loadingState.value = LoaderState.HideLoading
                        _errorState.value = it.exception
                    }
                }
            }
        }
    }

    override fun getMovieVideos(movieId: Int) {
        _loadingState.value = LoaderState.ShowLoading
        viewModelScope.launch {
            movieRepository.getMovieVideos(movieId).collect {
                when (it) {
                    is ResponseState.Success -> {
                        _loadingState.value = LoaderState.HideLoading
                        _movieVideos.value = it.data?.body()?.results
                    }

                    is ResponseState.Error -> {
                        _loadingState.value = LoaderState.HideLoading
                        _errorState.value = it.exception
                    }
                }
            }
        }
    }

}

sealed interface DetailViewModelContract {
    fun getMovieDetail(movieId: Int)
    fun getMovieReviews(movieId: Int)
    fun getMovieVideos(movieId: Int)
}