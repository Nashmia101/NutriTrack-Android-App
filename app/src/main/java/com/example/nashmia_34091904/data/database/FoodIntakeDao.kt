package com.example.nashmia_34091904.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nashmia_34091904.data.entities.FoodIntake

@Dao
interface FoodIntakeDao {

    @Insert
    suspend fun insert(intake: FoodIntake)

    @Query("SELECT * FROM food_intake WHERE userId = :userId LIMIT 1")
    suspend fun getByUserId(userId: Int): FoodIntake?

    @Query("SELECT * FROM food_intake WHERE userId = :userId LIMIT 1")
    suspend fun getFoodIntakeByUserId(userId: Int): FoodIntake?

    @Query("SELECT * FROM food_intake")
    suspend fun getAllFoodIntakes(): List<FoodIntake>
}