package com.example.moblie.Data.Network.API

import com.example.moblie.Data.Model.ApiResponse
import com.example.moblie.Data.Model.DTO.Request.ForgotPasswordRequest
import com.example.moblie.Data.Model.DTO.Request.LoginRequest
import com.example.moblie.Data.Model.DTO.Request.SendOtpRequest
import com.example.moblie.Data.Model.DTO.Request.SignUpRequest
import com.example.moblie.Data.Model.DTO.Response.UserResponse
import com.example.moblie.Data.Network.AUTH_EP
import com.example.moblie.Data.Network.OTP_EP
import com.example.moblie.Data.Network.USER_EP
import retrofit2.http.*
import retrofit2.Call

interface AccountAPI {
    @POST(USER_EP)
    fun signUp(@Body request: SignUpRequest): Call<ApiResponse<UserResponse>>
    @PUT("$USER_EP/forgotPassword")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ApiResponse<UserResponse>>

    @POST("$AUTH_EP/login")
    fun login(@Body request: LoginRequest): Call<ApiResponse<String>>

    @POST(OTP_EP)
    fun sendOtp(@Body request: SendOtpRequest): Call<ApiResponse<Any>>
}