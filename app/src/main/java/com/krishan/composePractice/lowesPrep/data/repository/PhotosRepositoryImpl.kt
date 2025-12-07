package com.krishan.composePractice.lowesPrep.data.repository

import com.krishan.composePractice.lowesPrep.domain.model.Photo
import com.krishan.composePractice.lowesPrep.domain.repo.PhotosRepository

class PhotosRepositoryImpl : PhotosRepository {
    override suspend fun fetchPhotos(): List<Photo> {
        TODO("Not yet implemented")
    }
}