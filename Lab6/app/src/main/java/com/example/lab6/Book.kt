package com.example.lab6

data class Book(
    val title: String,
    val author: String,
    val year: String,
    val coverId: Int
) : java.io.Serializable
