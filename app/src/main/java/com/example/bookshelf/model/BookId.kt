package com.example.bookshelf.model

import kotlinx.serialization.Serializable


@Serializable
data class BooksResponse(
    val items: List<BookId>
)

@Serializable
data class BookId(
    val id: String
)