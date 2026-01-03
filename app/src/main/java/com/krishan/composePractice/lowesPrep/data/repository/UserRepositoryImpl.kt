package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.data.dtos.toDomain
import com.krishan.composePractice.lowesPrep.data.remote.contracts.UserApiService
import com.krishan.composePractice.lowesPrep.domain.model.User
import com.krishan.composePractice.lowesPrep.domain.repo.UserRepository
import com.krishan.composePractice.pricelineprep.data.di.JsonPlaceholderRetrofit
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(@field:JsonPlaceholderRetrofit private val userApiService: UserApiService) : UserRepository {
    override suspend fun fetchUsers(): List<User> {
        return try {
            val response = userApiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                response.body()?.map { user -> user.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e("$e in ${javaClass.name}")
            emptyList()
        }
    }
}