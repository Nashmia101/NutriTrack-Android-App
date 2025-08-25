package com.example.nashmia_34091904.data.entities

import androidx.room.*

@Entity(
    tableName = "food_intake",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val fruits: Boolean,
    val vegetables: Boolean,
    val grains: Boolean,
    val redMeat: Boolean,
    val seafood: Boolean,
    val poultry: Boolean,
    val fish: Boolean,
    val eggs: Boolean,
    val nutsSeeds: Boolean,
    val persona: String,
    val biggestMealTime: String,
    val sleepTime: String,
    val wakeUpTime: String
)