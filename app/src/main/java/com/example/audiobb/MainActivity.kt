package com.example.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.DoubleLayout {

    var doubleFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("AudioBB")

        ViewModelProvider(this).get(BookViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, BookListFragment.newInstance(initBooks()))
            .addToBackStack(null)
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

    override fun selectionMade() {
        if (!doubleFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}