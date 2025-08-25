package com.example.nashmia_34091904.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NutriCoachTips")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)