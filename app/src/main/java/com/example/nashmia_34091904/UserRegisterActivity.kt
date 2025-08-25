package com.example.nashmia_34091904

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.nashmia_34091904.data.viewmodel.RegisterViewModel

import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme

class UserRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            RegisterViewModel.RegisterViewModelFactory(this)
        )[RegisterViewModel::class.java]

        viewModel.preloadCSVIfNeeded(this)

        setContent {
            Nashmia_34091904Theme {
                RegisterScreen(viewModel = viewModel, context = this)
            }
        }
    }
}

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, context: Context) {
    val userIds by viewModel.userIds.observeAsState(emptyList())
    val resultMsg by viewModel.registerResult.observeAsState()

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Register", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))


            Box(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedId?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("My ID (Provided by your Clinician)") },
                    readOnly = true,
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
                    onDismissRequest = { expanded = false }
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        userIds.forEach { id ->
                            DropdownMenuItem(
                                text = { Text(id.toString()) },
                                onClick = {
                                    selectedId = id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        password != confirmPassword ->
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()

                        selectedId == null || phoneNumber.isBlank() || password.isBlank() ->
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()

                        else ->
                            viewModel.register(selectedId!!, phoneNumber, name, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Register", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Login", color = Color.White)
            }

            if (resultMsg != null) {
                Spacer(Modifier.height(8.dp))
                Text(text = resultMsg ?: "", color = Color.Red)
            }
        }
    }
}
