package com.example.todoapp.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.TodoItem

class CustomDiffUtil(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}