package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.data.remote.contracts.GetUserService
import com.krishan.composePractice.lowesPrep.domain.model.User
import com.krishan.composePractice.lowesPrep.domain.repo.UserRepository

class UserRepositoryImpl(val service: GetUserService) : UserRepository {
    override suspend fun fetchUsers(): List<User> {
        TODO("Not yet implemented")
    }
}