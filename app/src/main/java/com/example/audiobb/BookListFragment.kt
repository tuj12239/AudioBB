package com.example.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListFragment : Fragment() {

    lateinit var bookList: BookList
    lateinit var layout: View
    lateinit var bookListView: RecyclerView

    companion object {
        @JvmStatic
        fun newInstance(_bookList: BookList): BookListFragment {

            val frag = BookListFragment().apply {
                bookList = _bookList

                arguments = Bundle().apply {
                    putSerializable("bookList", bookList)
                }
            }
            return frag
        }
    }

    interface DoubleLayout {
        fun selectionMade()
    }

    interface Search {
        fun makeSearch()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            bookList = it.getSerializable("bookList") as BookList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_book_list, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookListView = layout.findViewById(R.id.bookListView)
        bookListView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = BookAdapter(requireContext(), bookList) {
            updateModel(bookListView.getChildAdapterPosition(it))
        }
        bookListView.adapter = adapter

        val searchButton = layout.findViewById<Button>(R.id.searchDialogButton)
        searchButton.setOnClickListener { (requireActivity() as Search).makeSearch() }
    }

    private fun updateModel(index: Int) {
        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .setSelectedBook(bookList.get(index))
        (requireActivity() as DoubleLayout).selectionMade()
    }

}