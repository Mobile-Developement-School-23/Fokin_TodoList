package com.example.todoapp.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Importance
import com.example.todoapp.R
import com.example.todoapp.databinding.TodoitemPreviewBinding
import com.example.todoapp.TodoItem
import com.example.todoapp.ui.TodoListUiAction
import com.example.todoapp.utils.formatDate

class TodoItemViewHolder(binding: TodoitemPreviewBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var todoItem: TodoItem
    private val checkBox = binding.checkBox
    private val todoText = binding.todoText
    private val deadline = binding.deadlineText
    private val priorityIcon = binding.priorityImageView

    fun onBind(todoItem: TodoItem, onUiAction: (TodoListUiAction) -> Unit) {
        this.todoItem = todoItem
        checkBox.isChecked = todoItem.isDone
        todoText.text = todoItem.text

        val deadlineDate = todoItem.deadline
        if (deadlineDate != null) {
            deadline.text = formatDate(deadlineDate)
        }

        if (todoItem.importance == Importance.IMPORTANT) {
            priorityIcon.visibility = View.VISIBLE
            priorityIcon.setImageResource(R.drawable.priority_high_icon)
        } else if (todoItem.importance == Importance.LOW) {
            priorityIcon.visibility = View.VISIBLE
            priorityIcon.setImageResource(R.drawable.priority_low_icon)
        } else {
            priorityIcon.visibility = View.GONE
        }

        setupDiffCallback(onUiAction)
    }

    private fun setupDiffCallback(onUiAction: (TodoListUiAction) -> Unit) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (todoItem.isDone != isChecked) {
                val newItem = todoItem.copy(isDone = isChecked)
                onUiAction(TodoListUiAction.UpdateTodoItem(newItem))
            }
        }
        todoText.setOnClickListener {
            onUiAction(TodoListUiAction.EditTodoItem(todoItem))
        }
    }
}