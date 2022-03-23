package com.tnote.tnoteapp.models

data class TTElement(
    val created_at: String,
    val day: String,
    val description: String,
    val end: String,
    val id: Int,
    val repeating: Int,
    val start: String,
    val title: String,
    val ttid: Int,
    val updated_at: String
)