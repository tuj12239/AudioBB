package com.example.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("AudioBB")

        ViewModelProvider(this).get(BookViewModel::class.java)

        val listFragment = BookListFragment.newInstance(initBooks())
        val detailsFragment = BookDetailsFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, listFragment)
            .commit()

    }

    private fun initBooks(): BookList {
        val list = BookList()
        val names = resources.getStringArray(R.array.book_names)
        val authors = resources.getStringArray(R.array.book_authors)

        for (i in names.indices) {
            list.add(Book(names[i], authors[i]))
        }

        return list
    }
}