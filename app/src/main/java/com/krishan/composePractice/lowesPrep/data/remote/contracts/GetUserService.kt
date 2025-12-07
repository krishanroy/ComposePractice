package com.krishan.composePractice.lowesPrep.data.remote.contracts

import com.krishan.composePractice.lowesPrep.data.dtos.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface GetUserService {
    @GET("users")
    fun getUsers(): Response<List<UserDto>>
}