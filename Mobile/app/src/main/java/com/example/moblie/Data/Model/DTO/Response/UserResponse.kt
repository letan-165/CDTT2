package com.example.moblie.Data.Model.DTO.Response

data class UserResponse(
    val userID:String,
    val name: String,
    val password: String,
    val phone: String,
    val email: String,
    val role: String
)