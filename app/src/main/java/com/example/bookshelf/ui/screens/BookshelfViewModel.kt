package com.example.bookshelf.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BooksInfoRepository
import com.example.bookshelf.model.BookDetailItem
import kotlinx.coroutines.launch
import okio.IOException


sealed interface BookshelfUiState {
    data class Success(val details: List<BookDetailItem>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(
    private val booksInfoRepository: BooksInfoRepository
) : ViewModel() {

    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    var query by mutableStateOf("")
    var searchQuery by mutableStateOf("history")

    init {
        getDetails()
    }

    fun getDetails() {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            bookshelfUiState = try {
                BookshelfUiState.Success(booksInfoRepository.getBooksDetails(searchQuery))
            } catch (e: IOException) {
                BookshelfUiState.Error
            } catch (e: HttpException) {
                BookshelfUiState.Error
            }

        }
    }

    fun performSearch() {
        searchQuery = query
        getDetails()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksInfoRepository = application.container.booksInfoRepository
                BookshelfViewModel(booksInfoRepository = booksInfoRepository)
            }
        }
    }
}