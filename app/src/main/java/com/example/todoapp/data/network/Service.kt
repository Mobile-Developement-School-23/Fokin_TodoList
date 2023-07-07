package com.example.todoapp.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Service {

    @GET("list")
    suspend fun getAllTodoData(): Response<TodoListServerResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: String, @Body element: ItemContainer
    ): Response<ItemServerResponse>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") revision: String, @Path("id") id: String, @Body element: ItemContainer
    ): Response<ItemServerResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: String, @Path("id") id: String
    ): Response<ItemServerResponse>

    @PATCH("list")
    suspend fun patchList(
        @Header("X-Last-Known-Revision") revision: String, @Body list: TodoListContainer
    ): Response<TodoListServerResponse>
}