package com.tnote.tnoteapp.models

import com.example.retrofittest2.network.models.User

data class UserResponse(
    val user: User,
    val token: String
)
