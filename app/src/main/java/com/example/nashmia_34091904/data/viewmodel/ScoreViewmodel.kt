package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.*
import com.example.nashmia_34091904.data.entities.Patient
import com.example.nashmia_34091904.data.PatientRepository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreViewModel(private val context: Context) : ViewModel() {
    private val repo = ScoreRepository(context)

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> = _userId

    private val _totalScore = MutableLiveData<Float>()
    val totalScore: LiveData<Float> = _totalScore

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String> = _sex

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    fun loadUserData() {
        val currentUser = repo.getCurrentUserId().toIntOrNull() ?: return
        _userId.value = currentUser

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patient: Patient? = repo.getPatientByIdSuspend(currentUser)
                if (patient != null) {
                    _sex.postValue(patient.sex)
                    _name.postValue(patient.name ?: "")
                    val score = if (patient.sex.equals("Male", true))
                        patient.totalScoreMale ?: 0f
                    else
                        patient.totalScoreFemale ?: 0f
                    _totalScore.postValue(score)
                } else {
                    _totalScore.postValue(0f)
                }
            } catch (e: Exception) {
                _totalScore.postValue(0f)
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScoreViewModel(appContext) as T
        }
    }
}