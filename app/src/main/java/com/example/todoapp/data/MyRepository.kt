package com.example.todoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.network.ItemContainer
import com.example.todoapp.data.network.ItemServerResponse
import com.example.todoapp.data.network.TodoListContainer
import com.example.todoapp.data.network.TodoListServerResponse
import com.example.todoapp.data.network.MyWorkManager
import com.example.todoapp.data.db.RevisionDao
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.network.Service
import com.example.todoapp.di.AppScope
import com.example.todoapp.utils.createTodo
import com.example.todoapp.utils.toTodoItemServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@AppScope
class MyRepository @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val revisionDao: RevisionDao,
    private val workManager: MyWorkManager,
    private val apiService: Service
) : TodoItemsRepository {
    private val todoItems: MutableList<TodoItem> = mutableListOf()
    private val todoItemsFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(mutableListOf())

    private val errorListLiveData = MutableLiveData<Boolean>()
    private val errorItemLiveData = MutableLiveData<Boolean>()

    override suspend fun loadDataFromServer() =
        withContext(Dispatchers.IO) {
            val response = apiService.getAllTodoData()

            if (response.isSuccessful) {
                val dataFromServer = response.body() as TodoListServerResponse
                if (dataFromServer.revision > revisionDao.getCurrentRevision()) {
                    updateDataDB(dataFromServer)
                } else {
                    updateDataOnServer()
                }
                errorListLiveData.postValue(false)
            } else {
                loadDataFromDB()
                errorListLiveData.postValue(true)
            }
        }

    override suspend fun loadDataFromDB() =
        withContext(Dispatchers.IO) {
            updateTodoItems(todoItemDao.getAllTodoData().map { it.toTodoItem() })
            errorListLiveData.postValue(true)
        }

    override fun reloadData() {
        workManager.reloadData()
    }

    override fun errorListLiveData() = errorListLiveData

    override fun errorItemLiveData() = errorItemLiveData

    override suspend fun todoItems(): Flow<List<TodoItem>> = todoItemsFlow.asStateFlow()

    override suspend fun unfinishedTodoItems(): Flow<List<TodoItem>> {
        return flow {
            var unfinishedTodoItems = todoItems
            unfinishedTodoItems = unfinishedTodoItems.filter { !it.isDone } as MutableList<TodoItem>
            emit(unfinishedTodoItems)
        }
    }

    override suspend fun getTodoItem(id: String): TodoItem? = todoItems.firstOrNull { it.id == id }

    override suspend fun addTodoItem(todoItem: TodoItem) =
        withContext(Dispatchers.IO) {
            if (workManager.isNetworkAvailable()) {
                val todoItemServer = toTodoItemServer(todoItem)
                val response = apiService.addTodoItem(
                    revisionDao.getCurrentRevision().toString(), ItemContainer(todoItemServer)
                )
                updateRevision(response)
            }

            val newTodo = createTodo(todoItem)

            todoItemDao.insertNewTodoItemData(newTodo.toTodoDbEntity())
            todoItems.add(todoItem)
            updateFlow()
        }

    override suspend fun updateTodoItem(todoItem: TodoItem) =
        withContext(Dispatchers.IO) {
            val index = todoItems.indexOfFirst { it.id == todoItem.id }
            if (index != -1) {
                if (workManager.isNetworkAvailable()) {
                    val todoItemServer = toTodoItemServer(todoItem)
                    val response = apiService.updateTodoItem(
                        revisionDao.getCurrentRevision().toString(),
                        todoItem.id,
                        ItemContainer(todoItemServer)
                    )
                    updateRevision(response)
                }

                val updatedTodo = createTodo(todoItem)
                todoItemDao.updateTodoData(updatedTodo.toTodoDbEntity())
                todoItems[index] = todoItem
                updateFlow()
            }
        }

    override suspend fun removeTodoItem(id: String) =
        withContext(Dispatchers.IO) {
            val index = todoItems.indexOfFirst { it.id == id }
            if (index != -1) {
                if (workManager.isNetworkAvailable()) {
                    val response = apiService.deleteTodoItem(
                        revisionDao.getCurrentRevision().toString(), todoItems[index].id
                    )
                    updateRevision(response)
                }

                todoItemDao.deleteTodoDataById(id)
                todoItems.removeAt(index)
                updateFlow()
            }
        }

    private suspend fun updateDataOnServer() =
        withContext(Dispatchers.IO) {
            updateTodoItems(todoItemDao.getAllTodoData().map { it.toTodoItem() })

            val todoListServer = TodoListContainer(todoItems.map { toTodoItemServer(it) })
            val response = apiService.patchList(revisionDao.getCurrentRevision().toString(), todoListServer)

            if (response.isSuccessful) {
                val dataFromServer = response.body() as TodoListServerResponse
                revisionDao.updateRevision(dataFromServer.revision)
                errorItemLiveData.postValue(false)
            } else {
                errorListLiveData.postValue(true)
            }
        }

    private suspend fun updateDataDB(dataFromServer: TodoListServerResponse) =
        withContext(Dispatchers.IO) {
            val todoItemsFromServer =
                dataFromServer.list?.map { it.toTodoItem() }?.toMutableList() ?: listOf()

            revisionDao.updateRevision(dataFromServer.revision)
            updateTodoItems(todoItemsFromServer)

            val todoDbList = todoItems.map { createTodo(it).toTodoDbEntity() }
            todoItemDao.replaceAllTodoItems(todoDbList)
        }

    private suspend fun updateRevision(response: Response<ItemServerResponse>) =
        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val dataFromServer = response.body() as ItemServerResponse
                revisionDao.updateRevision(dataFromServer.revision)
            } else {
                errorItemLiveData.postValue(true)
            }
        }

    private fun updateTodoItems(newList: List<TodoItem>) {
        todoItems.clear()
        todoItems.addAll(newList)
        updateFlow()
    }

    private fun updateFlow() {
        todoItemsFlow.update {
            todoItems.toList()
        }
    }
}