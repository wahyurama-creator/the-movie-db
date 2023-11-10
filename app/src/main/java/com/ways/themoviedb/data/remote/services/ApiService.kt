package com.ways.themoviedb.data.remote.services

import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.response.review.ReviewResponse
import com.ways.themoviedb.data.remote.response.video.VideoResponse
import com.ways.themoviedb.data.remote.response.wrapper.WrapperResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getMovieList(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = true,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = SORT_BY_DESC,
    ): Response<WrapperResponse<MovieDetailResponse>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = LANGUAGE
    ): Response<MovieDetailResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReview(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = LANGUAGE
    ): Response<WrapperResponse<ReviewResponse>>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = LANGUAGE
    ): Response<WrapperResponse<VideoResponse>>

    private companion object {
        const val LANGUAGE = "en-US"
        const val SORT_BY_DESC = "popularity.desc"
    }

}