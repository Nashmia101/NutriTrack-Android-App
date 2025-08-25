package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.nashmia_34091904.data.entities.Patient
import com.example.nashmia_34091904.data.PatientRepository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsightsViewModel(private val context: Context) : ViewModel() {
    private val repository = ScoreRepository(context)

    private val _categoryScores = MutableLiveData<List<Pair<String, Float>>>()
    val categoryScores: LiveData<List<Pair<String, Float>>> = _categoryScores

    private val _totalScore = MutableLiveData<Float>()
    val totalScore: LiveData<Float> = _totalScore

    fun loadInsights() {
        val userId = repository.getCurrentUserId().toIntOrNull() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val patient = repository.getPatientByIdSuspend(userId)
            if (patient != null) {
                val scores = listOf(
                    "Vegetables" to (patient.vegetables ?: 0f),
                    "Fruits" to (patient.fruits ?: 0f),
                    "Grains & Cereals" to (patient.grains ?: 0f),
                    "Whole Grains" to (patient.wholeGrains ?: 0f),
                    "Meat & Alternatives" to (patient.meat ?: 0f),
                    "Dairy" to (patient.dairy ?: 0f),
                    "Water" to (patient.water ?: 0f),
                    "Unsaturated Fats" to (patient.unsaturatedFat ?: 0f),
                    "Sodium" to (patient.sodium ?: 0f),
                    "Sugar" to (patient.sugar ?: 0f),
                    "Alcohol" to (patient.alcohol ?: 0f),
                    "Discretionary Foods" to (patient.discretionary ?: 0f),
                    "Saturated Fats" to (patient.saturatedFat ?: 0f)
                )
                _categoryScores.postValue(scores)

                val total = if (patient.sex.equals("Male", true)) {
                    patient.totalScoreMale ?: 0f
                } else {
                    patient.totalScoreFemale ?: 0f
                }
                _totalScore.postValue(total)
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InsightsViewModel(appContext) as T
        }
    }
}

