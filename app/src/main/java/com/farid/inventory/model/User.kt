package com.farid.inventory.model

data class User(
    val id: Int,
    val name: String,
    val email: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: User
)
