package com.example.moblie.Data.Model.DTO.Request

data class SignUpRequest(
    val name: String,
    val password: String,
    val phone: String,
    val email: String,
    val role: String
)
