package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.data.dtos.toDomain
import com.krishan.composePractice.lowesPrep.data.remote.contracts.PhotosApiService
import com.krishan.composePractice.lowesPrep.domain.model.Photo
import com.krishan.composePractice.lowesPrep.domain.repo.PhotosRepository
import timber.log.Timber
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(private val photosApiService: PhotosApiService) : PhotosRepository {
    override suspend fun fetchPhotos(): List<Photo> {
        try {
            val response = photosApiService.getPhotos()
            return if (response.isSuccessful && response.body() != null) {
                response.body()?.map { photoDto -> photoDto.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e("$e in ${javaClass.name}")
            return emptyList()
        }
    }
}