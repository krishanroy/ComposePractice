package com.krishan.composePractice.lowesPrep.data.dtos

import com.krishan.composePractice.lowesPrep.domain.model.Photo

data class PhotoDto(val albumId: Int, val id: Int, val title: String, val url: String, val thumbnailUrl: String)

fun PhotoDto.toDomain(): Photo = Photo(
    id = this.id,
    albumId = this.albumId,
    title = this.title,
    url = this.url,
    thumbnailUrl = this.url
)