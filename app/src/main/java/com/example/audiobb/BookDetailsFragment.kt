package com.example.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class BookDetailsFragment : Fragment() {

    lateinit var layout: View
    lateinit var name: TextView
    lateinit var author: TextView

    companion object {
        @JvmStatic
        fun newInstance() = BookDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_book_details, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = layout.findViewById(R.id.bookTitleLabel)
        author = layout.findViewById(R.id.bookAuthorLabel)

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()
            .observe(viewLifecycleOwner, {updateLabels()})

    }

    private fun updateLabels() {
        val book = ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()

        name.text = book.value?.name
        author.text = book.value?.author
    }

}