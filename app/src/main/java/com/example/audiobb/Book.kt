package com.example.audiobb

import java.io.Serializable

data class Book (
    val name: String,
    val author: String,
    val id: Int,
    val coverURL: String,
    val duration: Int) : Serializable
