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
                val photos = response.body()?.take(20) ?: emptyList()
                // two ways to do it
                // 1st way ------>
//                photos.mapIndexed { index, photo ->
//                    val imageUrl = imageUrls[index]
//                    photo.copy(url = imageUrl).toDomain()
//                }
                // second way and more idiomatic in my opinion  ---->
                photos.zip(imageUrls) { photo, url -> photo.copy(url = url).toDomain() }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e("$e in ${javaClass.name}")
            return emptyList()
        }
    }
}

val imageUrls = listOf(
    "https://yavuzceliker.github.io/sample-images/image-1.jpg", // cat
    "https://i.imgur.com/OB0y6MR.jpg", // dog
    // Repeat with different placeholder variations or incrementing image IDs
    *List(18) { i ->
        "https://yavuzceliker.github.io/sample-images/image-${i + 1}.jpg"
    }.toTypedArray()
)