// Declaring package
package com.example.nashmia_34091904

// important imports for Android Compose UI, navigation, and styling.

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme

// MainActivity is the launch activity of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nashmia_34091904Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
// MainScreen composable displays the UI for the first screen

@Preview(showBackground = true)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFB3E5FC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))

                // App title
                Text(
                    text = "NutriTrack",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // App logo
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logo_app_2),
                    contentDescription = "logo app",
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // disclaimer text
                Text(
                    text = "This app provides general health and nutrition information for\n" +
                            "educational purposes only. It is not intended as medical advice,\n" +
                            "diagnosis, or treatment. Always consult a qualified healthcare\n" +
                            "professional before making any changes to your diet, exercise, or\n" +
                            "health regimen.\n" +
                            "Use this app at your own risk.\n" +
                            "If you’d like to an Accredited Practicing Dietitian (APD), please\n" +
                            "visit the Monash Nutrition/Dietetics Clinic (discounted rates for\n" +
                            "students):",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp

                )
                Spacer(modifier = Modifier.height(2.dp))

                // Clickable link to Monash nutrition clinic website
                Text(
                    text = "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"))
                        context.startActivity(intent)
                    }
                )
            }

               // Login button navigating to LoginActivity
                Button(
                    onClick = {
                        context.startActivity(Intent(context,LoginActivity::class.java))
                    },
                    modifier = Modifier
                        .padding(bottom = 14.dp)
                ) {
                    Text("Login")

                }

            Spacer(modifier = Modifier.height(2.dp))


            // credits
            Text(
                text = " Designed with ❤️ by Nashmia Shakeel (34091904)",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp

            )
            }
        }
    }











