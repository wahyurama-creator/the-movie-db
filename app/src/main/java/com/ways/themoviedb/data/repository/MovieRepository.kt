package com.ways.themoviedb.data.repository

import androidx.paging.PagingData
import com.ways.themoviedb.data.remote.response.genre.GenresResponse
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.data.remote.response.wrapper.WrapperResponse
import com.ways.themoviedb.data.remote.util.ResponseState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository {

    suspend fun getMovies(
        genre: String
    ): Flow<PagingData<MovieDetailResponse>>
    suspend fun getMovieDetail(movieId: Int): Flow<ResponseState<Response<MovieDetailResponse>>>
    suspend fun getMovieReviews(movieId: Int): Flow<ResponseState<Response<WrapperResponse<ReviewResponse>>>>
    suspend fun getMovieVideos(movieId: Int): Flow<ResponseState<Response<WrapperResponse<VideoResponse>>>>
    suspend fun getGenres(): Flow<ResponseState<Response<GenresResponse>>>

}