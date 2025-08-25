package com.example.nashmia_34091904.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nashmia_34091904.data.AIGen.UiState
import com.example.nashmia_34091904.data.PatientRepository.QuestionnaireRepository
import com.example.nashmia_34091904.data.PatientRepository.ScoreRepository
import com.example.nashmia_34091904.data.database.AppDatabase
import com.example.nashmia_34091904.data.entities.NutriCoachTip
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class AIViewmodel(private val context: Context) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyA95WHtJSxcOXMrOzTxuvJiMBGdbitKCbI"
    )

    fun sendPrompt(context: Context) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
                val userId = prefs.getString("currentUserId", null)?.toIntOrNull()
                if (userId == null) {
                    _uiState.postValue(UiState.Error("User ID not found"))
                    return@launch
                }

                val repo = ScoreRepository(context)
                val patient = repo.getPatientByIdSuspend(userId)
                val questionnaireRepo = QuestionnaireRepository(context)
                val intake = questionnaireRepo.getFoodIntake()

                if (patient == null || intake == null) {
                    _uiState.postValue(UiState.Error("Could not load patient or food intake data"))
                    return@launch
                }

                val prompt = buildString {
                    append("Generate a short encouraging message to help someone improve their fruit intake. ")
                    append("Here is their health and diet information:\n\n")
                    append("HEIFA Scores:\n")
                    append("Fruits: ${patient.fruits}, Vegetables: ${patient.vegetables}, Grains: ${patient.grains}, Whole Grains: ${patient.wholeGrains}, Meat: ${patient.meat}, Dairy: ${patient.dairy}, Water: ${patient.water}, Sugar: ${patient.sugar}, Sodium: ${patient.sodium}, Alcohol: ${patient.alcohol}, Discretionary: ${patient.discretionary}, Saturated Fat: ${patient.saturatedFat}\n\n")
                    append("Food Intake Preferences:\n")
                    append("Fruits: ${intake.fruits}, Vegetables: ${intake.vegetables}, Grains: ${intake.grains}, Red Meat: ${intake.redMeat}, Seafood: ${intake.seafood}, Poultry: ${intake.poultry}, Fish: ${intake.fish}, Eggs: ${intake.eggs}, Nuts/Seeds: ${intake.nutsSeeds}\n\n")
                    append("Persona: ${intake.persona}\n")
                    append("Biggest Meal Time: ${intake.biggestMealTime}, Sleep Time: ${intake.sleepTime}, Wake-up Time: ${intake.wakeUpTime}")
                }

                val response = generativeModel.generateContent(content { text(prompt) })
                val result = response.text

                if (result != null) {
                    saveTipToDatabase(userId, result)
                    _uiState.postValue(UiState.Success(result))
                } else {
                    _uiState.postValue(UiState.Error("No response from AI"))
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    fun sendDataPatternPrompt() {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patients = ScoreRepository(context).getAllPatientsSuspend()
                val intakes  = QuestionnaireRepository(context).getAllFoodIntakes()

                if (patients.isEmpty() || intakes.isEmpty()) {
                    _uiState.postValue(UiState.Error("No data available for patterns"))
                    return@launch
                }

                val prompt = buildString {
                    appendLine("Here is the complete dataset for all users (CSV):")
                    appendLine("UserID,Sex,TotalMale,TotalFemale,Fruits,Vegetables,Grains,WholeGrains,Meat,Dairy,Water,Sugar,Sodium,Alcohol,Discretionary,SatFat")
                    patients.forEach { p ->
                        appendLine(listOf(
                            p.userId, p.sex, p.totalScoreMale, p.totalScoreFemale,
                            p.fruits, p.vegetables, p.grains, p.wholeGrains,
                            p.meat, p.dairy, p.water, p.sugar, p.sodium,
                            p.alcohol, p.discretionary, p.saturatedFat
                        ).joinToString(","))
                    }
                    appendLine("IntakeID,UserID,Fruits,Vegetables,Grains,RedMeat,Seafood,Poultry,Fish,Eggs,NutsSeeds,Persona,BiggestMealTime,SleepTime,WakeUpTime")
                    intakes.forEach { i ->
                        appendLine(listOf(
                            i.id, i.userId,
                            i.fruits, i.vegetables, i.grains,
                            i.redMeat, i.seafood, i.poultry, i.fish, i.eggs, i.nutsSeeds,
                            i.persona, i.biggestMealTime, i.sleepTime, i.wakeUpTime
                        ).joinToString(","))
                    }
                    appendLine("Please identify the three most interesting data-driven patterns across users.")
                }

                val response = generativeModel.generateContent(content { text(prompt) })
                val text     = response.text.orEmpty().trim()

                if (text.isBlank()) {
                    _uiState.postValue(UiState.Error("Empty response from AI"))
                } else {
                    _uiState.postValue(UiState.Success(text))
                }

            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }



    private fun saveTipToDatabase(userId: Int, message: String) {
        val tip = NutriCoachTip(userId = userId, message = message)
        val db = AppDatabase.getDatabase(context)
        viewModelScope.launch(Dispatchers.IO) {
            db.nutriCoachTipDao().insertTip(tip)
        }
    }

    fun getTipsForUser(): LiveData<List<NutriCoachTip>> {
        val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val userId = prefs.getString("currentUserId", null)?.toIntOrNull() ?: return MutableLiveData(emptyList())
        val db = AppDatabase.getDatabase(context)
        return db.nutriCoachTipDao().getTipsForUser(userId)
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AIViewmodel(context) as T
        }
    }
}