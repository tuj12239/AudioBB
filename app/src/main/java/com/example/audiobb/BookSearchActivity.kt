package com.example.audiobb

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val searchField = findViewById<EditText>(R.id.searchView)
        val button = findViewById<Button>(R.id.searchButton)
        button.setOnClickListener{fetchBooks(searchField.text.toString())}
    }

    val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    private fun fetchBooks(search: String) {
        val url = "https://kamorris.com/lab/cis3515/search.php?term=$search"

        Log.d("Event", "Fetching JSON from $url")

        Thread{
            kotlin.run {
                volleyQueue.add(
                JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    {onJSONReceived(it)},
                    { Toast.makeText(this, "Couldn't get JSON", Toast.LENGTH_SHORT).show()}
                )
            ) }
        }.start()


    }

    private fun onJSONReceived(json: JSONArray) {
        Log.d("Response", json.toString())

        try {
            //TODO Update views here
        } catch(j: JSONException) {j.printStackTrace()}
    }
}