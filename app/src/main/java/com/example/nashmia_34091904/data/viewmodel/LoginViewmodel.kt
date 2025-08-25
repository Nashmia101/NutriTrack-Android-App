package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nashmia_34091904.data.PatientRepository.PatientRepository
import com.example.nashmia_34091904.data.entities.Patient
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: PatientRepository) : ViewModel() {


    val allUserIds: LiveData<List<Int>> = repository.allUserIds.asLiveData()


    private val _patient = MutableLiveData<Patient?>()
    val patient: LiveData<Patient?> = _patient

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun login(userId: Int, password: String) {
        viewModelScope.launch {
            val result = repository.getByIdAndPassword(userId, password)
            if (result != null) {
                _patient.postValue(result)
            } else {
                _errorMessage.postValue("Invalid ID or password. Please register first.")
            }
        }
    }

    fun setErrorMessage(msg: String) {
        _errorMessage.value = msg
    }
}

class LoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val appContext = context.applicationContext
    private val repository = PatientRepository(appContext)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}