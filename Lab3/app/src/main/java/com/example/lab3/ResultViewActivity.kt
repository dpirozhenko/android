package com.example.lab3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3.storage.ResultDatabaseHelper

class ResultViewActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var emptyText: TextView
    private lateinit var db: ResultDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_view)

        listView = findViewById(R.id.resultList)
        emptyText = findViewById(R.id.emptyText)
        val buttonClear = findViewById<Button>(R.id.buttonClear)
        db = ResultDatabaseHelper(this)

        buttonClear.setOnClickListener {
            val success = db.clearAllResults()
            if (success) {
                Toast.makeText(this, "Усі записи очищено", Toast.LENGTH_SHORT).show()
                updateList()
            } else {
                Toast.makeText(this, "Помилка очищення", Toast.LENGTH_SHORT).show()
            }
        }

        updateList()
    }

    private fun updateList() {
        val results = db.getAllResults()
        if (results.isEmpty()) {
            listView.visibility = ListView.GONE
            emptyText.visibility = TextView.VISIBLE
        } else {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, results)
            listView.adapter = adapter
            listView.visibility = ListView.VISIBLE
            emptyText.visibility = TextView.GONE
        }
    }
}
