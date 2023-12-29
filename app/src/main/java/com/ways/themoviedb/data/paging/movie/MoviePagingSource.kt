package com.ways.themoviedb.data.paging.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ways.themoviedb.data.remote.response.movie.MovieDetailResponse
import com.ways.themoviedb.data.remote.services.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val apiService: ApiService,
    private val genre: String
) : PagingSource<Int, MovieDetailResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDetailResponse>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetailResponse> {
        val pageIndex = params.key ?: 1
        return try {
            val response = apiService.getMovieList(page = pageIndex, genre = genre)
            val responseData = mutableListOf<MovieDetailResponse>()
            val data = response.body()?.results ?: emptyList()
            val totalPage = response.body()?.totalPages ?: 0
            responseData.addAll(data)

            val nextKey = if (pageIndex >= totalPage) null else pageIndex + 1

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}