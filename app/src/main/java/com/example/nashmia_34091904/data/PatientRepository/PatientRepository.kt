package com.example.nashmia_34091904.data.PatientRepository

import android.content.Context
import com.example.nashmia_34091904.data.database.AppDatabase
import com.example.nashmia_34091904.data.entities.Patient
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context) {


    private val patientDao = AppDatabase.getDatabase(context).patientDao()


    val allUserIds: Flow<List<Int>> = patientDao.getAllUserIds()


    suspend fun insertPatient(patient: Patient) {
        patientDao.insertPatient(patient)
    }


    suspend fun updatePatient(patient: Patient) {
        patientDao.updatePatient(patient)
    }


    suspend fun getByIdAndPhone(userId: Int, phone: String): Patient? {
        return patientDao.getPatientByIdAndPhone(userId, phone)
    }

    suspend fun getByIdAndPassword(userId: Int, password: String): Patient? {
        return patientDao.getByIdAndPassword(userId, password)
    }

    suspend fun getAverageScoreBySex(sex: String): Float {
        return patientDao.getAverageScoreBySex(sex) ?: 0f
    }
}