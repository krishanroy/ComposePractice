package com.krishan.composePractice.lowesPrep.domain.repo

import com.krishan.composePractice.lowesPrep.domain.model.Photo

interface PhotosRepository {
    suspend fun fetchPhotos(): List<Photo>
}