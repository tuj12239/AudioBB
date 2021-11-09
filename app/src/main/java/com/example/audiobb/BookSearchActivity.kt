package com.example.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BookSearchActivity : AppCompatActivity() {

    val bookList = BookList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val searchField = findViewById<EditText>(R.id.searchView)
        val button = findViewById<Button>(R.id.searchButton)
        button.setOnClickListener{fetchBooks(searchField.text.toString())}
    }

    private val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    private fun fetchBooks(search: String) {
        val url = "https://kamorris.com/lab/cis3515/search.php?term=$search"

        Log.d("Event", "Fetching JSON from $url")

        volleyQueue.add(
            JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                {onJSONReceived(it)},
                { Toast.makeText(this, "Couldn't get JSON", Toast.LENGTH_SHORT).show()}
            )
        )
    }

    private fun onJSONReceived(json: JSONArray) {
        Log.d("Response", json.toString())

        try {
            for (i in 0 until json.length()) {
                val jsonBook = json.getJSONObject(i)
                bookList.add(Book(
                    jsonBook.getString("title"),
                    jsonBook.getString("author"),
                    jsonBook.getInt("id"),
                    jsonBook.getString("cover_url")
                ))

                Log.i("Added book:", bookList.get(i).name)
            }

            val resIntent = intent
            resIntent.putExtra("bookjson", bookList)
            setResult(RESULT_OK, resIntent)
            finish()
        } catch(j: JSONException) {j.printStackTrace()}
    }
}