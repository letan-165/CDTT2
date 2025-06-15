package com.example.moblie.Data.Model;

data class ApiResponse<T> (
    val code: Int,
    val message: String,
    val result: T,
)
