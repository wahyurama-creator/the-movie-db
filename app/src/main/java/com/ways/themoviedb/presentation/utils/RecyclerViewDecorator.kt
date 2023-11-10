package com.ways.themoviedb.presentation.utils

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class RecyclerViewDecorator(
    private val columnCount: Int = 1,
    private val spacing: Int,
    private val includeEdge: Boolean = false,
    private val context: Context
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        setDecoration(outRect, position % columnCount, position)
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).roundToInt()
    }

    private fun setDecoration(rect: Rect, column: Int, position: Int) {
        val convertedSpacing = dpToPx(spacing)
        if (includeEdge) {
            rect.left = convertedSpacing - column * convertedSpacing / columnCount
            rect.right = (column + 1) * convertedSpacing / columnCount

            if (position < columnCount)
                rect.top = convertedSpacing

            rect.bottom = convertedSpacing
        } else {
            rect.left = column * convertedSpacing / columnCount
            rect.right = convertedSpacing - (column + 1) * convertedSpacing / columnCount
            if (position >= columnCount)
                rect.top = convertedSpacing
        }
    }
}