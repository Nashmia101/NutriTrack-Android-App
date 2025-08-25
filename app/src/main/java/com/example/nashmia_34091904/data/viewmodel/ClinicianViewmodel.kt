package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nashmia_34091904.data.PatientRepository.PatientRepository

import kotlinx.coroutines.launch


class ClinicianViewModel(private val context: Context) : ViewModel(){

    private val _enteredKey = MutableLiveData<String>("")
    val enteredKey: LiveData<String> = _enteredKey

    private val _isAuthenticated = MutableLiveData<Boolean?>(null)
    val isAuthenticated: LiveData<Boolean?> = _isAuthenticated

    private val _maleAverage = MutableLiveData<Float>()
    val maleAverage: LiveData<Float> = _maleAverage

    private val _femaleAverage = MutableLiveData<Float>()
    val femaleAverage: LiveData<Float> = _femaleAverage

    private val repo = PatientRepository(context)

    fun updateKey(key: String) {
        _enteredKey.value = key
    }

    fun validateKey() {
        _isAuthenticated.value = _enteredKey.value == "dollar-entry-apples"
    }

    fun loadAverages() {
        viewModelScope.launch {
            _maleAverage.postValue(repo.getAverageScoreBySex("Male"))
            _femaleAverage.postValue(repo.getAverageScoreBySex("Female"))
        }
    }
}

class ClinicianViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClinicianViewModel(context) as T
    }
}