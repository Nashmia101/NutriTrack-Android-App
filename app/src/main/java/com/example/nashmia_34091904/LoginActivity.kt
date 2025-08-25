package com.example.nashmia_34091904

import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import com.example.nashmia_34091904.data.PatientRepository.PatientRepository
import com.example.nashmia_34091904.data.database.AppDatabase
import com.example.nashmia_34091904.data.viewmodel.LoginViewModel
import com.example.nashmia_34091904.data.viewmodel.LoginViewModelFactory
import com.example.nashmia_34091904.data.viewmodel.RegisterViewModel
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme



class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = AppDatabase.getDatabase(this)
        val repository = PatientRepository(this)


        val registerViewModel = RegisterViewModel(this)
        registerViewModel.preloadCSVIfNeeded(this)


        val viewModelFactory = LoginViewModelFactory(this)

        setContent {
            Nashmia_34091904Theme {
                LoginScreen(factory = viewModelFactory)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(factory: LoginViewModelFactory) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val userIds by viewModel.allUserIds.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState()
    val patient by viewModel.patient.observeAsState()

    var selectedId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


    LaunchedEffect(patient) {
        patient?.let {
            val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("currentUserId", it.userId.toString())
                .putString("currentUserPhone", it.phoneNumber)
                .putString("currentUserName", it.name ?: "Unknown")
                .apply()

            context.startActivity(Intent(context, QuestionnaireActivity::class.java))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color(0xFFB3E5FC))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Log in", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))


        Box(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedId,
                onValueChange = {},
                readOnly = true,
                label = { Text("My ID (Provided by your Clinician)") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null,
                        modifier = Modifier.clickable { expanded = true })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
            ) {
                userIds.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id.toString()) },
                        onClick = {
                            selectedId = id.toString()
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account on your first visit.",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                val idInt = selectedId.toIntOrNull()
                if (idInt != null && password.isNotBlank()) {
                    viewModel.login(idInt, password)
                } else {
                    viewModel.setErrorMessage("Please fill all fields correctly.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {
                context.startActivity(Intent(context, UserRegisterActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))


        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}