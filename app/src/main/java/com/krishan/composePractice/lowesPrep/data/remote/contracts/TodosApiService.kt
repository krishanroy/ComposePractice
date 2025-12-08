package com.krishan.composePractice.lowesPrep.data.remote.contracts

import com.krishan.composePractice.lowesPrep.data.dtos.TodoDto
import retrofit2.Response
import retrofit2.http.GET

interface TodosApiService {
    @GET("todos")
    suspend fun getTodos(): Response<List<TodoDto>>
}