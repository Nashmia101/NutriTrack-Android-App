package com.example.nashmia_34091904.data.PatientRepository

import android.content.Context
import com.example.nashmia_34091904.data.entities.FoodIntake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.nashmia_34091904.data.database.AppDatabase

class QuestionnaireRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao = db.foodIntakeDao()
    private val userPrefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
    private val userId = userPrefs.getString("currentUserId", "0")?.toIntOrNull() ?: 0
    private val sharedPref = context.getSharedPreferences("questionnaire_sp_$userId", Context.MODE_PRIVATE)

    fun loadChecked(index: Int): Boolean = sharedPref.getBoolean("checked$index", false)
    fun saveChecked(index: Int, value: Boolean) = sharedPref.edit().putBoolean("checked$index", value).apply()
    fun loadPersona(): String = sharedPref.getString("persona", "") ?: ""
    fun savePersona(value: String) = sharedPref.edit().putString("persona", value).apply()
    fun loadTime(key: String): String = sharedPref.getString(key, "") ?: ""
    fun saveTime(key: String, value: String) = sharedPref.edit().putString(key, value).apply()

    fun insertFoodIntake(
        fruits: Boolean, vegetables: Boolean, grains: Boolean,
        redMeat: Boolean, seafood: Boolean, poultry: Boolean,
        fish: Boolean, eggs: Boolean, nutsSeeds: Boolean,
        persona: String, biggestMealTime: String,
        sleepTime: String, wakeUpTime: String
    ) {
        val intake = FoodIntake(
            userId = userId,
            fruits = fruits,
            vegetables = vegetables,
            grains = grains,
            redMeat = redMeat,
            seafood = seafood,
            poultry = poultry,
            fish = fish,
            eggs = eggs,
            nutsSeeds = nutsSeeds,
            persona = persona,
            biggestMealTime = biggestMealTime,
            sleepTime = sleepTime,
            wakeUpTime = wakeUpTime
        )
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(intake)
        }
    }

    suspend fun getFoodIntake(): FoodIntake? {
        return dao.getFoodIntakeByUserId(userId)
    }

    suspend fun getAllFoodIntakes() = dao.getAllFoodIntakes()
}
