package com.example.bookfinder20

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookfinder20.RetrofitInstance.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class BookSearchUiState(
    val searchQuery: String = "",
    val books: List<Item> = emptyList(),
    val selectedBook: Item? = null
)


class BookViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _uiState = MutableLiveData(BookSearchUiState())
    val uiState: LiveData<BookSearchUiState> = _uiState

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value?.copy(searchQuery = query)
    }

    fun selectBook(book: Item) {
        _uiState.value = _uiState.value?.copy(selectedBook = book)
    }

    fun unselectBook() {
        _uiState.value = _uiState.value?.copy(selectedBook = null)
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = bookRepository.fetchBooks(query)
                _uiState.postValue(_uiState.value?.copy(books = response))
            } catch (e: Exception) {
                // ... error handling ...
                e.printStackTrace()
            }
        }
    }
}

class BookRepository {
    private val apiService = retrofit.create(BookApiService::class.java)
    suspend fun fetchBooks(query: String): List<Item> {
        return apiService.searchBooks(query).items
    }
}

class ViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


