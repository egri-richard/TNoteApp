package com.tnote.tnoteapp.models

data class TTElement(
    val created_at: String?,
    var day: String,
    var description: String,
    var end: String,
    val id: Int?,
    var repeating: Boolean,
    var start: String,
    var title: String,
    val ttid: Int,
    val updated_at: String?
)