package com.ways.themoviedb.presentation.main.viewModel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ways.themoviedb.data.remote.response.genre.Genre
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.util.ResponseState
import com.ways.themoviedb.data.repository.MovieRepository
import com.ways.themoviedb.data.utils.LoaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel(), MainViewModelContract, DefaultLifecycleObserver {

    private var _loadingState = MutableLiveData<LoaderState>()
    private var _error = MutableLiveData<String?>()
    private var _genres = MutableLiveData<List<Genre>>()
    private var _selectedGenre = MutableLiveData<Genre>()

    val loadingState: LiveData<LoaderState> = _loadingState
    val error: LiveData<String?> = _error
    val genres: LiveData<List<Genre>> = _genres
    val selectedGenre: LiveData<Genre> = _selectedGenre

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getGenres()
    }

    override fun getGenres() {
        _loadingState.value = LoaderState.ShowLoading
        viewModelScope.launch {
            movieRepository.getGenres().collect { response ->
                when (response) {
                    is ResponseState.Success -> {
                        val responseBody = response.data
                        if (responseBody?.body()?.genres != null) {
                            _genres.postValue(responseBody.body()?.genres)
                            _selectedGenre.postValue(responseBody.body()?.genres?.first())
                            _loadingState.postValue(LoaderState.HideLoading)
                        } else {
                            _error.postValue(response.message)
                            _loadingState.postValue(LoaderState.HideLoading)
                        }
                    }

                    is ResponseState.Error -> {
                        _error.postValue(response.exception)
                        _loadingState.postValue(LoaderState.HideLoading)
                    }
                }
            }
        }
    }

    override suspend fun getMovies(genre: String): Flow<PagingData<MovieDetailResponse>> {
        return movieRepository.getMovies(genre).cachedIn(viewModelScope).flowOn(Dispatchers.IO)
    }

    override fun setSelectedGenre(genre: Genre) {
        _selectedGenre.value = genre
    }

    override fun setError(error: String) {
        _error.value = error
    }

}

sealed interface MainViewModelContract {
    fun getGenres()
    suspend fun getMovies(genre: String): Flow<PagingData<MovieDetailResponse>>
    fun setSelectedGenre(genre: Genre)
    fun setError(error: String)
}