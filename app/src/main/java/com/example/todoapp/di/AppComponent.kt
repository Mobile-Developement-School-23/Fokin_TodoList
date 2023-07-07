package com.example.todoapp.di

import dagger.BindsInstance
import dagger.Component
import android.content.Context
import com.example.todoapp.App
import com.example.todoapp.data.network.AccessibleNetworkWorker
import com.example.todoapp.data.network.DataCheckWorker
import com.example.todoapp.data.network.InaccessibleNetworkWorker

@AppScope
@Component(modules = [
    DataModule::class,
    WorkerModule::class,
    ApiServiceModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun todoListFragmentComponent(): TodoListComponent
    fun addTodoItemFragmentComponent(): ChangeTodoItemComponent

    fun inject(todoApp: App)
    fun inject(worker: DataCheckWorker)
    fun inject(worker: AccessibleNetworkWorker)
    fun inject(worker: InaccessibleNetworkWorker)
}