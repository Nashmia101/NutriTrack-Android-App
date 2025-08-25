package com.example.nashmia_34091904.data.PatientRepository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.nashmia_34091904.data.database.AppDatabase
import com.example.nashmia_34091904.data.entities.FoodIntake
import com.example.nashmia_34091904.data.entities.NutriCoachTip
import com.example.nashmia_34091904.data.entities.Patient

class ScoreRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val patientDao = db.patientDao()

    fun getCurrentUserId(): String {
        val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        return prefs.getString("currentUserId", "User") ?: "User"
    }

    suspend fun getPatientByIdSuspend(userId: Int): Patient? {
        return patientDao.getPatientById(userId)
    }

    suspend fun insertTip(tip: NutriCoachTip) {
        db.nutriCoachTipDao().insertTip(tip)
    }

    fun getAllTips(userId: Int): LiveData<List<NutriCoachTip>> {
        return db.nutriCoachTipDao().getTipsForUser(userId)
    }

    suspend fun getAllPatientsSuspend() = patientDao.getAllPatients()

}