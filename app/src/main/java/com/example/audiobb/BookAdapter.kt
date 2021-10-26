package com.example.audiobb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(_context: Context, _books: BookList, _vocl: View.OnClickListener) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val context = _context
    private val books = _books
    private val inflater = LayoutInflater.from(context)
    private val vocl = _vocl

    class ViewHolder(_view: View) : RecyclerView.ViewHolder(_view) {
        val view = _view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recyclerviewelement, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val titleLabel = holder.view.findViewById<TextView>(R.id.listTitleLabel)
        titleLabel.setText(books.get(position).name)
        val authorLabel = holder.view.findViewById<TextView>(R.id.listAuthorLabel)
        authorLabel.setText(books.get(position).author)
        holder.view.setOnClickListener(vocl)
    }

    override fun getItemCount(): Int {
        return books.size()
    }


}