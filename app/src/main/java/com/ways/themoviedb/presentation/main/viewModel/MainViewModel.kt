package com.ways.themoviedb.presentation.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel(), MainViewModelContract {

    override suspend fun getMovies(): Flow<PagingData<MovieDetailResponse>> {
        return movieRepository.getMovies().cachedIn(viewModelScope).flowOn(Dispatchers.IO)
    }

}

sealed interface MainViewModelContract {
    suspend fun getMovies(): Flow<PagingData<MovieDetailResponse>>
}