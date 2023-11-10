package com.ways.themoviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ways.themoviedb.data.paging.movie.MoviePagingSource
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.data.remote.response.wrapper.WrapperResponse
import com.ways.themoviedb.data.remote.services.ApiService
import com.ways.themoviedb.data.remote.util.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {

    override suspend fun getMovies(): Flow<PagingData<MovieDetailResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 24,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(apiService)
            },
            initialKey = 1
        ).flow.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovieDetail(movieId: Int): Flow<ResponseState<Response<MovieDetailResponse>>> {
        return flow {
            try {
                val response = apiService.getMovieDetail(movieId)

                if (response.isSuccessful) {
                    emit(ResponseState.Success(response))
                } else {
                    emit(ResponseState.Error(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                emit(ResponseState.Error(e.message.toString()))
            } catch (e: IOException) {
                emit(ResponseState.Error("No Internet Connection"))
            }
        }
    }

    override suspend fun getMovieReviews(movieId: Int): Flow<ResponseState<Response<WrapperResponse<ReviewResponse>>>> {
        return flow {
            try {
                val response = apiService.getMovieReview(movieId)

                if (response.isSuccessful) {
                    emit(ResponseState.Success(response))
                } else {
                    emit(ResponseState.Error(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                emit(ResponseState.Error(e.message.toString()))
            } catch (e: IOException) {
                emit(ResponseState.Error("No Internet Connection"))
            }
        }
    }

    override suspend fun getMovieVideos(movieId: Int): Flow<ResponseState<Response<WrapperResponse<VideoResponse>>>> {
        return flow {
            try {
                val response = apiService.getMovieVideo(movieId)

                if (response.isSuccessful) {
                    emit(ResponseState.Success(response))
                } else {
                    emit(ResponseState.Error(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                emit(ResponseState.Error(e.message.toString()))
            } catch (e: IOException) {
                emit(ResponseState.Error("No Internet Connection"))
            }
        }
    }

}