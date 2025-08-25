package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nashmia_34091904.data.database.AppDatabase
import com.example.nashmia_34091904.data.entities.Patient
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.io.BufferedReader
import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.nashmia_34091904.data.PatientRepository.PatientRepository
import kotlinx.coroutines.Dispatchers



class RegisterViewModel(context: Context) : ViewModel() {
    private val repo = PatientRepository(context)


    val userIds: LiveData<List<Int>> = repo.allUserIds.asLiveData()

    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> = _registerResult


    fun preloadCSVIfNeeded(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        if (prefs.getBoolean("csv_loaded", false)) return

        viewModelScope.launch(Dispatchers.IO) {
            val inputStream = context.assets.open("2081.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.readLines()
            val headers = lines.first().split(",")

            lines.drop(1).forEach { line ->
                val cols = line.split(",")
                val userId = cols[1].toIntOrNull() ?: return@forEach
                val phone = cols[0].trim()
                val sex = cols[2].trim()
                val name = cols[3].trim()

                val scoreSuffix = if (sex.equals("Male", ignoreCase = true)) "Male" else "Female"

                val patient = Patient(
                    userId = userId,
                    phoneNumber = phone,
                    name = name,
                    sex = sex,
                    vegetables = cols.getOrNull(headers.indexOf("VegetablesHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    fruits = cols.getOrNull(headers.indexOf("FruitHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    grains = cols.getOrNull(headers.indexOf("GrainsandcerealsHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    wholeGrains = cols.getOrNull(headers.indexOf("WholegrainsHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    meat = cols.getOrNull(headers.indexOf("MeatandalternativesHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    dairy = cols.getOrNull(headers.indexOf("DairyandalternativesHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    water = cols.getOrNull(headers.indexOf("WaterHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    unsaturatedFat = cols.getOrNull(headers.indexOf("UnsaturatedFatHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    sodium = cols.getOrNull(headers.indexOf("SodiumHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    sugar = cols.getOrNull(headers.indexOf("SugarHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    alcohol = cols.getOrNull(headers.indexOf("AlcoholHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    discretionary = cols.getOrNull(headers.indexOf("DiscretionaryHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    saturatedFat = cols.getOrNull(headers.indexOf("SaturatedFatHEIFAscore$scoreSuffix"))?.toFloatOrNull(),
                    totalScoreMale = cols.getOrNull(headers.indexOf("HEIFAtotalscoreMale"))?.toFloatOrNull(),
                    totalScoreFemale = cols.getOrNull(headers.indexOf("HEIFAtotalscoreFemale"))?.toFloatOrNull(),
                    password = null
                )
                repo.insertPatient(patient)
            }
            prefs.edit().putBoolean("csv_loaded", true).apply()
        }
    }




    fun register(userId: Int, phone: String, name: String, password: String) {
        viewModelScope.launch {
            val patient = repo.getByIdAndPhone(userId, phone)

            when {
                patient == null -> {
                    _registerResult.postValue("Invalid ID or phone number.")
                }
                !patient.password.isNullOrEmpty() -> {
                    _registerResult.postValue("This account has already been registered.")
                }
                else -> {
                    repo.updatePatient(patient.copy(name = name, password = password))
                    _registerResult.postValue("Account successfully registered!")
                }
            }
        }
    }


    class RegisterViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RegisterViewModel(context) as T
    }
}