package com.example.moblie.Data.Service

import android.util.Log
import com.example.moblie.Data.Model.ApiResponse
import com.example.moblie.Data.Model.DTO.Request.ForgotPasswordRequest
import com.example.moblie.Data.Model.DTO.Request.SignUpRequest
import com.example.moblie.Data.Model.DTO.Response.UserResponse
import com.example.moblie.Data.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserService {
    fun signUp(
        request: SignUpRequest,
        onSuccess: (UserResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.accountAPI.signUp(request).enqueue(object : Callback<ApiResponse<UserResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<UserResponse>>,
                response: Response<ApiResponse<UserResponse>>
            ) {

                if (response.isSuccessful) {
                    val user = response.body()?.result
                    user?.let { onSuccess(it) } ?: onError("Không có dữ liệu")
                } else {
                    onError("Đăng kí thất bại")
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserResponse>>, t: Throwable) {
                Log.d("API", "Tải thất bại", t)
                onError("Có sự cố khi tải")
            }
        })
    }

    fun forgotPassword(
        request: ForgotPasswordRequest,
        onSuccess: (UserResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.accountAPI.forgotPassword(request).enqueue(object : Callback<ApiResponse<UserResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<UserResponse>>,
                response: Response<ApiResponse<UserResponse>>
            ) {

                if (response.isSuccessful) {
                    val user = response.body()?.result
                    user?.let { onSuccess(it) } ?: onError("Không có dữ liệu")
                } else {
                    onError("Cập nhật thất bại")
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserResponse>>, t: Throwable) {
                Log.d("API", "Tải thất bại", t)
                onError("Có sự cố khi tải")
            }
        })
    }
}
