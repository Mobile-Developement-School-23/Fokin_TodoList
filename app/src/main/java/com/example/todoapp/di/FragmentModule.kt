package com.example.todoapp.di

import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.ui.ChangeTodoItemViewModel
import com.example.todoapp.ui.TodoItemAdapter
import com.example.todoapp.ui.TodoItemDecoration
import com.example.todoapp.ui.TodoListViewModel
import com.example.todoapp.utils.toPx
import dagger.Module
import dagger.Provides

@Module
interface FragmentModule {
    companion object {
        @FragmentScope
        @Provides
        fun provideTodoListViewModel(repository: TodoItemsRepository): TodoListViewModel {
            return TodoListViewModel(repository)
        }

        @FragmentScope
        @Provides
        fun provideChangeTodoItemViewModel(repository: TodoItemsRepository): ChangeTodoItemViewModel {
            return ChangeTodoItemViewModel(repository)
        }
        @FragmentScope
        @Provides
        fun providePreviewOffsetDecoration(): TodoItemDecoration {
            return TodoItemDecoration(bottomOffset = 16f.toPx.toInt())
        }

        @FragmentScope
        @Provides
        fun provideTodoItemsAdapter(viewModel: TodoListViewModel): TodoItemAdapter {
            return TodoItemAdapter(viewModel::onUiAction)
        }
    }
}