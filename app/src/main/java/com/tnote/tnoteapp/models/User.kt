package com.tnote.tnoteapp.models

import java.time.LocalDateTime

data class User(
    var id: Int,
    var name: String,
    var email: String,
    var password: String,
    var created_at: String
)
