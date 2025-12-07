package com.krishan.composePractice.lowesPrep.domain.repo

import com.krishan.composePractice.lowesPrep.domain.model.Todo

interface TodosRepository {
    suspend fun fetchTodos(): List<Todo>
}