package com.example.todoapp.di

import com.example.todoapp.ui.TodoListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface TodoListComponent {
    fun inject(fragment: TodoListFragment)
}