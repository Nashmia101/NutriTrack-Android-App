package com.example.nashmia_34091904.data.database

import com.example.nashmia_34091904.data.entities.Fruit
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityApiService {
    @GET("fruit/{name}")
    suspend fun getFruitByName(@Path("name") name: String): Fruit
}