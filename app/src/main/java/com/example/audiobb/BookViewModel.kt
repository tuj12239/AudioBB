package com.example.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {

    private val selectedBook: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }

    private val bookProgress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getSelectedBook(): LiveData<Book> {
        return selectedBook
    }

    fun setSelectedBook(book: Book) {
        selectedBook.value = book
    }

    fun getBookProgress(): LiveData<Int> {
        return bookProgress
    }

    fun setBookProgress(progress: Int) {
        bookProgress.value = progress
    }
}