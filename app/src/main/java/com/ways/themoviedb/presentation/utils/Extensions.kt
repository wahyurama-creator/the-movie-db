package com.ways.themoviedb.presentation.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.DisplayMetrics

// calculate the column based on screen width and each item row width in DP
fun Context.calculateNumberOfGridColumn(itemWidthDp: Int): Int {
    val displayMetrics: DisplayMetrics = this.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / itemWidthDp).toInt()
}

// simplify parcelable without warning
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}