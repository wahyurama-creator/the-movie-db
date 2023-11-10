package com.ways.themoviedb.data.remote.response.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailResponse(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>?,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountries>?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>?,
    @SerializedName("tagline")
    val tagline: String?,
): Parcelable