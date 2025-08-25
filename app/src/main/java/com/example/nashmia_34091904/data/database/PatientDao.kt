package com.example.nashmia_34091904.data.database

import androidx.room.*
import com.example.nashmia_34091904.data.entities.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatient(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientById(userId: Int): Patient?

    @Query("SELECT * FROM patients WHERE userId = :userId AND phoneNumber = :phone")
    suspend fun getPatientByIdAndPhone(userId: Int, phone: String): Patient?

    @Query("SELECT userId FROM patients ORDER BY userId")
    fun getAllUserIds(): Flow<List<Int>>

    @Query("SELECT * FROM patients WHERE userId = :userId AND password = :password")
    suspend fun getByIdAndPassword(userId: Int, password: String): Patient?

    @Query("""
    SELECT AVG(
        CASE 
            WHEN sex = 'Male' THEN totalScoreMale
            WHEN sex = 'Female' THEN totalScoreFemale
        END
    )
    FROM patients
    WHERE sex = :sex
""")
    suspend fun getAverageScoreBySex(sex: String): Float?

    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>
}