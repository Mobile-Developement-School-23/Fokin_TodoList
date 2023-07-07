package com.example.todoapp.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TodoItemDecoration (
    private val leftOffset: Int = 5,
    private val topOffset: Int = 5,
    private val rightOffset: Int = 5,
    private val bottomOffset: Int = 5
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset)
    }
}