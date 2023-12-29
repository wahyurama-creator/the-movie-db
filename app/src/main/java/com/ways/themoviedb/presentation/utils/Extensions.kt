package com.ways.themoviedb.presentation.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ways.themoviedb.databinding.BottomSheetErrorWithActionBinding

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

fun AppCompatActivity.showBottomSheet(
    title: String,
    description: String,
    layoutInflater: LayoutInflater,
    isCancelable: Boolean = true,
    onCancelAction: (() -> Unit)? = null
) {
    if (isDestroyed || isFinishing) return
    val dialog = BottomSheetDialog(this).apply {
        setCancelable(isCancelable)
        setOnCancelListener { onCancelAction?.invoke() }
    }

    val binding = BottomSheetErrorWithActionBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)

    with(binding) {
        tvErrorTitle.text = title
        tvErrorDescription.text = description
    }
    lifecycle.addObserver(DialogDismissLifecycleObserver(dialog))
    dialog.show()
}

private class DialogDismissLifecycleObserver(private var dialog: BottomSheetDialog?) :
    DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        dialog?.dismiss()
        dialog = null
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        dialog?.dismiss()
        dialog = null
    }
}