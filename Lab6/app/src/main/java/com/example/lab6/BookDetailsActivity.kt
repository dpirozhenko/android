package com.example.lab6

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class BookDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        val image = findViewById<ImageView>(R.id.detailsCover)
        val title = findViewById<TextView>(R.id.detailsTitle)
        val author = findViewById<TextView>(R.id.detailsAuthor)
        val year = findViewById<TextView>(R.id.detailsYear)

        val book = intent.getSerializableExtra("book") as Book

        title.text = book.title
        author.text = book.author
        year.text = "Рік публікації: ${book.year}"

        val imageUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg"
        Glide.with(this).load(imageUrl).into(image)
    }
}
