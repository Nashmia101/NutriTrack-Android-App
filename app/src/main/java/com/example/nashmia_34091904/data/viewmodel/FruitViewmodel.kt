package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nashmia_34091904.data.Client.FruityApiClient
import com.example.nashmia_34091904.data.PatientRepository.PatientRepository
import com.example.nashmia_34091904.data.PatientRepository.ScoreRepository
import com.example.nashmia_34091904.data.entities.Fruit
import kotlinx.coroutines.launch

class NutriCoachViewModel(private val context: Context) : ViewModel() {

    private val _fruit = MutableLiveData<Fruit?>()
    val fruit: LiveData<Fruit?> = _fruit

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    val _inputText = MutableLiveData<String>("")
    val inputText: LiveData<String> = _inputText

    private val _fruitScore = MutableLiveData<Float?>()
    val fruitScore: LiveData<Float?> = _fruitScore

    fun fetchFruitInfo(name: String) {
        viewModelScope.launch {
            try {
                val result = FruityApiClient.api.getFruitByName(name.lowercase())
                _fruit.postValue(result)
                _error.postValue(null)
            } catch (e: Exception) {
                _fruit.postValue(null)
                _error.postValue("Fruit not found or API error.")
            }
        }
    }

    fun loadFruitScore(context: Context) {
        val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val userId = prefs.getString("currentUserId", null)?.toIntOrNull() ?: return
        viewModelScope.launch {
            val repo = ScoreRepository(context)
            val patient = repo.getPatientByIdSuspend(userId)
            _fruitScore.postValue(patient?.fruits)
        }
    }
}

class NutriCoachViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NutriCoachViewModel(context) as T
    }
}
