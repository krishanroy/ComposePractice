package com.krishan.composePractice.lowesPrep.data.remote.contracts

import com.krishan.composePractice.lowesPrep.data.dtos.TodoDto
import retrofit2.Response
import retrofit2.http.GET

interface GetTodosService {
    @GET("todos")
    fun getTodos(): Response<List<TodoDto>>
}