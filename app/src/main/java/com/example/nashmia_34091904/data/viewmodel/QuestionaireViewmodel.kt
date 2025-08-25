package com.example.nashmia_34091904.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nashmia_34091904.data.PatientRepository.QuestionnaireRepository


class QuestionnaireViewModel(
    private val repository: QuestionnaireRepository
) : ViewModel() {
    private val _checkedStates = List(9) { index -> MutableLiveData(repository.loadChecked(index + 1)) }
    val checkedStates: List<LiveData<Boolean>> = _checkedStates
    private val _persona = MutableLiveData(repository.loadPersona())
    val persona: LiveData<String> = _persona
    private val _biggestMealTime = MutableLiveData(repository.loadTime("biggestMealTime"))
    val biggestMealTime: LiveData<String> = _biggestMealTime
    private val _sleepTime = MutableLiveData(repository.loadTime("sleepTime"))
    val sleepTime: LiveData<String> = _sleepTime
    private val _wakeUpTime = MutableLiveData(repository.loadTime("wakeUpTime"))
    val wakeUpTime: LiveData<String> = _wakeUpTime

    fun setChecked(index: Int, value: Boolean) {
        _checkedStates[index-1].value = value
        repository.saveChecked(index, value)
    }
    fun setPersona(value: String) {
        _persona.value = value
        repository.savePersona(value)
    }
    fun setBiggestMealTime(value: String) {
        _biggestMealTime.value = value
        repository.saveTime("biggestMealTime", value)
    }
    fun setSleepTime(value: String) {
        _sleepTime.value = value
        repository.saveTime("sleepTime", value)
    }
    fun setWakeUpTime(value: String) {
        _wakeUpTime.value = value
        repository.saveTime("wakeUpTime", value)
    }
    fun submitResponses() {
        repository.insertFoodIntake(
            fruits = _checkedStates[0].value ?: false,
            vegetables = _checkedStates[1].value ?: false,
            grains = _checkedStates[2].value ?: false,
            redMeat = _checkedStates[3].value ?: false,
            seafood = _checkedStates[4].value ?: false,
            poultry = _checkedStates[5].value ?: false,
            fish = _checkedStates[6].value ?: false,
            eggs = _checkedStates[7].value ?: false,
            nutsSeeds = _checkedStates[8].value ?: false,
            persona = _persona.value ?: "",
            biggestMealTime = _biggestMealTime.value ?: "",
            sleepTime = _sleepTime.value ?: "",
            wakeUpTime = _wakeUpTime.value ?: ""
        )
    }
}

class QuestionnaireViewModelFactory(
    private val repository: QuestionnaireRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionnaireViewModel::class.java)) {
            return QuestionnaireViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

