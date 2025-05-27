package com.example.lab3.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ResultDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "results.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "shape TEXT," +
                    "options TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS results")
        onCreate(db)
    }

    fun insertResult(shape: String, options: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("shape", shape)
        values.put("options", options)
        return db.insert("results", null, values) != -1L
    }

    fun getAllResults(): List<String> {
        val results = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT shape, options FROM results", null)
        if (cursor.moveToFirst()) {
            do {
                val shape = cursor.getString(0)
                val options = cursor.getString(1)
                results.add("Фігура: $shape | Обрано: $options")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    fun clearAllResults(): Boolean {
        val db = writableDatabase
        return db.delete("results", null, null) >= 0
    }
}
