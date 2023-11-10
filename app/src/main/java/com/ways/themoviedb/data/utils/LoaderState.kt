package com.ways.themoviedb.data.utils

sealed class LoaderState {
    data object ShowLoading : LoaderState()
    data object HideLoading : LoaderState()
}