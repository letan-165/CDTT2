package com.example.moblie.Data.Service

import android.util.Log
import com.example.moblie.Data.Model.ApiResponse
import com.example.moblie.Data.Model.DTO.Request.SendOtpRequest
import com.example.moblie.Data.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object OtpService {
    fun sendOtp(
        request: SendOtpRequest,
        onSuccess: ()-> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.accountAPI.sendOtp(request).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Gửi mã xác nhận thất bại")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Log.d("API", "Tải thất bại", t)
                onError("Có sự cố khi tải")
            }
        })
    }
}