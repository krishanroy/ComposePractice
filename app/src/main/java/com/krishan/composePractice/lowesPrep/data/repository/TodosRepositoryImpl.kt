package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.domain.model.Todo
import com.krishan.composePractice.lowesPrep.domain.repo.TodosRepository

class TodosRepositoryImpl : TodosRepository {
    override suspend fun fetchTodos(): List<Todo> {
        TODO("Not yet implemented")
    }
}