package com.krishan.composePractice.lowesPrep.data.remote.contracts

import com.krishan.composePractice.lowesPrep.data.dtos.PhotoDto
import retrofit2.Response
import retrofit2.http.GET

interface GetPhotosService {
    @GET("photos")
    fun getPhotos(): Response<List<PhotoDto>>
}