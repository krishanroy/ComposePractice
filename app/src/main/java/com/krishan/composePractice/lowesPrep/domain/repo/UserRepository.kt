package com.krishan.composePractice.lowesPrep.domain.repo

import com.krishan.composePractice.lowesPrep.domain.model.User

interface UserRepository {
    suspend fun fetchUsers(): List<User>
}