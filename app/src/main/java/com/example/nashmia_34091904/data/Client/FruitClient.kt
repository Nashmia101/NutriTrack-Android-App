package com.example.nashmia_34091904.data.Client

import com.example.nashmia_34091904.data.database.FruityApiService
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
object FruityApiClient {
    private const val BASE_URL = "https://www.fruityvice.com/api/"

    val api: FruityApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruityApiService::class.java)
    }
}