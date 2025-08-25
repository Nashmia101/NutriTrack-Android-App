package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nashmia_34091904.data.PatientRepository.ScoreRepository
import com.example.nashmia_34091904.data.entities.Patient
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: ScoreRepository) : ViewModel() {

    private val _patient = MutableLiveData<Patient?>()
    val patient: LiveData<Patient?> = _patient

    fun loadCurrentUser(userId: Int) {
        viewModelScope.launch {
            val user = repository.getPatientByIdSuspend(userId)
            _patient.postValue(user)
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = ScoreRepository(context)
            return SettingsViewModel(repo) as T
        }
    }
}