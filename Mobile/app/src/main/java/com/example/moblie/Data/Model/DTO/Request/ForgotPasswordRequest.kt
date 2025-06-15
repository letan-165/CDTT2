package com.example.moblie.Data.Model.DTO.Request

data class ForgotPasswordRequest (
    val username: String,
    val email: String,
    val password: String,
    val otp: Int,
)