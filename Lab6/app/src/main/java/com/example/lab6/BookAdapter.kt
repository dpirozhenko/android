package com.example.lab6

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide

class BookAdapter(private val context: Context, private val books: List<Book>) :
    BaseAdapter() {

    override fun getCount(): Int = books.size
    override fun getItem(position: Int): Any = books[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_book, parent, false)

        val book = books[position]

        val title = view.findViewById<TextView>(R.id.bookTitle)
        val author = view.findViewById<TextView>(R.id.bookAuthor)
        val year = view.findViewById<TextView>(R.id.bookYear)
        val image = view.findViewById<ImageView>(R.id.bookCover)

        title.text = book.title
        author.text = book.author
        year.text = "Рік: ${book.year}"

        val imageUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
        Glide.with(context).load(imageUrl).into(image)

        return view
    }
}
