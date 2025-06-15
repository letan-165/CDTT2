package com.example.moblie.Data.Network

import com.example.moblie.Data.Network.API.AccountAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val accountAPI: AccountAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL + ACCOUNT_EP)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountAPI::class.java)
    }
}
