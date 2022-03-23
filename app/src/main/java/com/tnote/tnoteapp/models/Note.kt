package com.tnote.tnoteapp.models

data class Note(
    var content: String,
    val created_at: String?,
    val id: Int?,
    val ownerId: Int,
    var title: String,
    val updated_at: String?
)