package com.example.moblie.Data.Service

import android.util.Log
import com.example.moblie.Data.Model.ApiResponse
import com.example.moblie.Data.Model.DTO.Request.ForgotPasswordRequest
import com.example.moblie.Data.Model.DTO.Request.LoginRequest
import com.example.moblie.Data.Model.DTO.Request.SignUpRequest
import com.example.moblie.Data.Model.DTO.Response.UserResponse
import com.example.moblie.Data.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthService {
    fun login(
        request: LoginRequest,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.accountAPI.login(request).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {

                if (response.isSuccessful) {
                    val token = response.body()?.result
                    token?.let { onSuccess(it) } ?: onError("Không có dữ liệu")
                } else {
                    onError("Đăng nhập thất bại")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("API", "Tải thất bại", t)
                onError("Có sự cố khi tải")
            }
        })
    }


}