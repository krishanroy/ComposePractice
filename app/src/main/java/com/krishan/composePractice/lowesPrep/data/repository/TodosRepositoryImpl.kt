package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.data.dtos.toDomain
import com.krishan.composePractice.lowesPrep.data.remote.contracts.TodosApiService
import com.krishan.composePractice.lowesPrep.domain.model.Todo
import com.krishan.composePractice.lowesPrep.domain.repo.TodosRepository
import timber.log.Timber
import javax.inject.Inject

class TodosRepositoryImpl @Inject constructor(private val todosApiService: TodosApiService) : TodosRepository {
    override suspend fun fetchTodos(): List<Todo> {
        return try {
            val response = todosApiService.getTodos()
            if (response.isSuccessful && response.body() != null) {
                response.body()?.map { todo -> todo.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e("$e in ${javaClass.name}")
            emptyList()
        }
    }
}