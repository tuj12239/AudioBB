package com.example.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {

    private val selectedBook: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }

    private val bookProgress: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    fun getSelectedBook(): LiveData<Book> {
        return selectedBook
    }

    fun setSelectedBook(book: Book) {
        selectedBook.value = book
    }

}