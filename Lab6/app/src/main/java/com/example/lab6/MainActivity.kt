package com.example.lab6

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var editSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var bookList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editSearch = findViewById(R.id.editSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        bookList = findViewById(R.id.bookList)

        buttonSearch.setOnClickListener {
            val query = editSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchBooks(query)
            } else {
                Toast.makeText(this, "Введіть запит", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchBooks(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = fetchBooks(query)
            withContext(Dispatchers.Main) {
                if (result.isNotEmpty()) {
                    val adapter = BookAdapter(this@MainActivity, result)
                    bookList.adapter = adapter

                    bookList.setOnItemClickListener { _, _, position, _ ->
                        val intent = Intent(this@MainActivity, BookDetailsActivity::class.java)
                        intent.putExtra("book", result[position])
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Нічого не знайдено", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchBooks(query: String): List<Book> {
        val client = OkHttpClient()
        val url = "https://openlibrary.org/search.json?q=${query.replace(" ", "+")}"
        val request = Request.Builder().url(url).build()

        return try {
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: "")
            val docs = json.getJSONArray("docs")

            val result = mutableListOf<Book>()
            for (i in 0 until minOf(docs.length(), 20)) {
                val book = docs.getJSONObject(i)
                val title = book.optString("title", "Без назви")
                val author = book.optJSONArray("author_name")?.optString(0) ?: "Автор невідомий"
                val year = book.optString("first_publish_year", "—")
                val coverId = book.optInt("cover_i", 0)

                result.add(Book(title, author, year, coverId))
            }
            result
        } catch (e: Exception) {
            listOf()
        }
    }
}
