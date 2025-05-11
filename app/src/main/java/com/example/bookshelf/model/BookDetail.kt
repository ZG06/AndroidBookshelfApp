package com.example.bookshelf.model

import kotlinx.serialization.Serializable


@Serializable
data class BookDetail(
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val imageLinks: ImageLinks
)

@Serializable
data class ImageLinks(
    val thumbnail: String
)