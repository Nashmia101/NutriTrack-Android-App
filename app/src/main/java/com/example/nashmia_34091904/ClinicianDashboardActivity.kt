package com.example.nashmia_34091904
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nashmia_34091904.data.AIGen.UiState
import com.example.nashmia_34091904.data.viewmodel.AIViewmodel
import com.example.nashmia_34091904.data.viewmodel.ClinicianViewModel
import com.example.nashmia_34091904.data.viewmodel.ClinicianViewModelFactory
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme

class ClinicianDashboardActivity : ComponentActivity() {
    private val clinicianVm: ClinicianViewModel by viewModels {
        ClinicianViewModelFactory(this)
    }
    private val aiVm: AIViewmodel by viewModels {
        AIViewmodel.Factory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clinicianVm.loadAverages()
        setContent {
            Nashmia_34091904Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClinicianDashboardScreen(
                        clinicianVm = clinicianVm,
                        aiVm         = aiVm
                    )
                }
            }
        }
    }
}


@Composable
fun ClinicianDashboardScreen(
    clinicianVm: ClinicianViewModel,
    aiVm: AIViewmodel
) {
    val maleAvg by clinicianVm.maleAverage.observeAsState(0f)
    val femaleAvg by clinicianVm.femaleAverage.observeAsState(0f)
    val uiState by aiVm.uiState.observeAsState(UiState.Initial)
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Clinician Dashboard",
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            ScoreRow("Average HEIFA (Male)", maleAvg)
            Spacer(Modifier.height(12.dp))
            ScoreRow("Average HEIFA (Female)", femaleAvg)

            Spacer(Modifier.height(24.dp))


            Button(
                onClick = { aiVm.sendDataPatternPrompt() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Find Data Pattern")
            }

            Spacer(Modifier.height(16.dp))

            when (uiState) {
                UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Error -> {
                    Text(
                        text = (uiState as UiState.Error).errorMessage,
                        color = Color.Red
                    )
                }

                is UiState.Success -> {
                    val resultText = (uiState as UiState.Success).outputText
                    val patterns = resultText.lines()
                        .map { it.trim() }
                        .filter { it.matches(Regex("^\\d+\\.\\s.*")) }
                        .take(3)

                    patterns.forEach { pattern ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = MaterialTheme.shapes.medium)
                                .padding(16.dp)
                        ) {
                            Text(pattern)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                else -> { /* Initial */ }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }


        Button(
            onClick = {
                val activity = context as Activity
                activity.startActivity(Intent(context, LoginActivity::class.java))
                activity.finish()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Done")
        }
    }
}

@Composable
fun ScoreRow(label: String, score: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text("%.1f".format(score))
    }
}