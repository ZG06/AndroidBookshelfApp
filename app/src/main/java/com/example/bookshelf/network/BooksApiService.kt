package com.example.bookshelf.network

import com.example.bookshelf.model.BookDetail
import com.example.bookshelf.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BooksApiService {

    @GET(".")
    suspend fun getBooks(
        @Query("q") query: String = "history"
    ): BooksResponse

    @GET("{id}")
    suspend fun getBookDetail(
        @Path("id") id: String
    ): BookDetail
}