package com.example.todoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TodoitemPreviewBinding
import com.example.todoapp.data.TodoItem
import com.example.todoapp.utils.TODO_ITEM_ADAPTER_PREVIEW_TYPE

class TodoItemAdapter(private val onUiAction: (TodoListActions) -> Unit) : RecyclerView.Adapter<TodoItemViewHolder>() {
    private var oldTodoItemsList = emptyList<TodoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return when (viewType) {
            TODO_ITEM_ADAPTER_PREVIEW_TYPE -> TodoItemViewHolder(
                TodoitemPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw java.lang.IllegalArgumentException("something wrong with view type")
        }
    }

    override fun getItemCount() = oldTodoItemsList.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = oldTodoItemsList[position]
        holder.onBind(todoItem, onUiAction)
    }

    fun setData(newTodoItemsList: List<TodoItem>) {
        val diffUtil = MyDiffUtil(oldTodoItemsList, newTodoItemsList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldTodoItemsList = newTodoItemsList
        diffResult.dispatchUpdatesTo(this)
    }
}