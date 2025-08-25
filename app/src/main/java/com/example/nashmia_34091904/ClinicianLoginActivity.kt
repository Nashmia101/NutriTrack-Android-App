package com.example.nashmia_34091904

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nashmia_34091904.data.viewmodel.ClinicianViewModel
import com.example.nashmia_34091904.data.viewmodel.ClinicianViewModelFactory
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme

class ClinicianLoginActivity : ComponentActivity() {
    private val clinicianViewModel: ClinicianViewModel by viewModels {
        ClinicianViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nashmia_34091904Theme {
                ClinicianLoginScreen(viewModel = clinicianViewModel)
            }
        }
    }
}

@Composable
fun ClinicianLoginScreen(viewModel: ClinicianViewModel) {
    val context = LocalContext.current
    val key by viewModel.enteredKey.observeAsState("")
    val isAuthenticated by viewModel.isAuthenticated.observeAsState()


    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == true) {
            context.startActivity(Intent(context, ClinicianDashboardActivity::class.java))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF90CAF9))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Clinician Login", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = key,
            onValueChange = viewModel::updateKey,
            label = { Text("Clinician Key") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.validateKey() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Icon(Icons.Default.Lock, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Clinician Login")
        }

        if (isAuthenticated == false) {
            Spacer(Modifier.height(16.dp))
            Text("Invalid key. Please try again.", color = Color.Red)
        }
    }
}