package com.example.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.DoubleLayout {

    var doubleFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("AudioBB")

        ViewModelProvider(this).get(BookViewModel::class.java)

        doubleFragment = findViewById<FragmentContainerView>(R.id.fragmentContainerView2) != null

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView1, BookListFragment.newInstance(initBooks()))
            .addToBackStack(null)
            .commit()

        if (doubleFragment) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView2, BookDetailsFragment.newInstance())
                .commit()
        }

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
                .replace(R.id.fragmentContainerView1, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}