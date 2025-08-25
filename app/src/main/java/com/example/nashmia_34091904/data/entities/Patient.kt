package com.example.nashmia_34091904.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey val userId: Int,
    val phoneNumber: String,
    var name: String?,
    val sex: String,
    val totalScoreMale: Float?,
    val totalScoreFemale: Float?,
    val vegetables: Float?,
    val fruits: Float?,
    val grains: Float?,
    val wholeGrains: Float?,
    val meat: Float?,
    val dairy: Float?,
    val water: Float?,
    val unsaturatedFat: Float?,
    val sodium: Float?,
    val sugar: Float?,
    val alcohol: Float?,
    val discretionary: Float?,
    val saturatedFat: Float?,
    val password: String? = null
)