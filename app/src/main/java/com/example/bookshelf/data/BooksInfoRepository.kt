package com.example.bookshelf.data

import com.example.bookshelf.model.BookId
import com.example.bookshelf.model.BooksResponse
import com.example.bookshelf.network.BooksApiService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface BooksInfoRepository {
    suspend fun getBooksIds(query: String): List<BookId>
    suspend fun getBooksThumbnails(query: String): MutableList<String>
}

class NetworkBooksInfoRepository(
    private val booksApiService: BooksApiService
) : BooksInfoRepository {
    override suspend fun getBooksIds(query: String): List<BookId> {
        val response: BooksResponse = booksApiService.getBooks(query)

        return response.items.map { BookId(it.id) }
    }

    override suspend fun getBooksThumbnails(query: String): MutableList<String> = coroutineScope {
        val ids = getBooksIds(query)

        val deferreds: List<Deferred<String?>> = ids.map { bookId ->
            async {
                try {
                    booksApiService.getBookDetail(bookId.id).volumeInfo.imageLinks.thumbnail.replace("http", "https")
                } catch (_: Exception) {
                    null  // Return null if there is an exception
                }
            }
        }

        deferreds.mapNotNull { it.await() }.toMutableList()  // Filter out nulls
    }


}