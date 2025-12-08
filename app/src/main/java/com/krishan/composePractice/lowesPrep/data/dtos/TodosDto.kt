package com.krishan.composePractice.lowesPrep.data.dtos

import com.krishan.composePractice.lowesPrep.domain.model.Todo

data class TodoDto(val userId: String, val id: Int, val title: String, val completed: Boolean)

fun TodoDto.toDomain(): Todo {
    return Todo(
        userId = this.userId,
        id = this.id,
        title = this.title,
        isCompleted = this.completed
    )
}