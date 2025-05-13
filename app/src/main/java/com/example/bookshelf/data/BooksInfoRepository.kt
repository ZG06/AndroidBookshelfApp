package com.example.bookshelf.data

import com.example.bookshelf.model.BookDetailItem
import com.example.bookshelf.model.BookId
import com.example.bookshelf.model.BooksResponse
import com.example.bookshelf.network.BooksApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface BooksInfoRepository {
    suspend fun getBooksIds(query: String): List<BookId>
    suspend fun getBooksDetails(query: String): List<BookDetailItem>
}

class NetworkBooksInfoRepository(
    private val booksApiService: BooksApiService
) : BooksInfoRepository {
    override suspend fun getBooksIds(query: String): List<BookId> {
        val response: BooksResponse = booksApiService.getBooks(query)

        return response.items.map { BookId(it.id) }
    }

    override suspend fun getBooksDetails(query: String): List<BookDetailItem> = coroutineScope {
        val ids = getBooksIds(query)

        val deferred = ids.map { bookId ->
            async {
                try {
                    val detail = booksApiService.getBookDetail(bookId.id).volumeInfo
                    BookDetailItem(
                        title = detail.title,
                        description = detail.description,
                        thumbnail = detail.imageLinks.thumbnail.replace("http", "https")
                    )
                } catch (_: Exception) {
                    null
                }
            }
        }

        deferred.mapNotNull { it.await() }
    }
}