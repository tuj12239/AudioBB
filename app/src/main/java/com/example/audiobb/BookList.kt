package com.example.audiobb

import java.io.Serializable

class BookList : Serializable {
    private val list = ArrayList<Book>()

    fun add(book: Book) {
        list.add(book)
    }

    fun remove(book: Book) {
        list.remove(book)
    }

    fun get(index: Int): Book {
        return list[index]
    }

    fun size(): Int {
        return list.size
    }

    fun clear() {
        list.clear()
    }
}