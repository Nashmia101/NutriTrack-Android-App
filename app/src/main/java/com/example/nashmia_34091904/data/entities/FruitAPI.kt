package com.example.nashmia_34091904.data.entities

data class Fruit(
    val name: String,
    val family: String,
    val genus: String,
    val order: String,
    val nutritions: Nutrition
)

data class Nutrition(
    val carbohydrates: Double,
    val protein: Double,
    val fat: Double,
    val calories: Int,
    val sugar: Double
)