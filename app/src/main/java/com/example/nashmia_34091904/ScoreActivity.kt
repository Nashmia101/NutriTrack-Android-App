
package com.example.nashmia_34091904
import coil.compose.rememberAsyncImagePainter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nashmia_34091904.data.AIGen.UiState
import com.example.nashmia_34091904.data.viewmodel.AIViewmodel
import com.example.nashmia_34091904.data.viewmodel.ScoreViewModel
import com.example.nashmia_34091904.data.viewmodel.InsightsViewModel
import com.example.nashmia_34091904.data.viewmodel.SettingsViewModel
import com.example.nashmia_34091904.data.viewmodel.NutriCoachViewModel
import com.example.nashmia_34091904.data.viewmodel.NutriCoachViewModelFactory
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme


class ScoreActivity : ComponentActivity() {
    private val fruitViewModel: NutriCoachViewModel by viewModels {
        NutriCoachViewModelFactory(this)
    }
    private val aiViewModel: AIViewmodel by viewModels { AIViewmodel.Factory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nashmia_34091904Theme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
                        composable("home") { ScoreScreen(navController) }
                        composable("Insights") { InsightsScreen(navController) }
                        composable("NutriCoach") { NutriCoachScreen(fruitViewModel, aiViewModel) }
                        composable("Settings") { SettingsScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: ScoreViewModel = viewModel(factory = ScoreViewModel.Factory(context))
    val name by viewModel.name.observeAsState("User")
    val score by viewModel.totalScore.observeAsState(0f)

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp)
    ) {
        Text(text = "Hello,", fontSize = 18.sp)
        Text(text = name, fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                context.startActivity(Intent(context, QuestionnaireActivity::class.java))
            }) {
                Text("Edit")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(id = R.drawable.portion),
            contentDescription = "portion",
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("My Score", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("See all scores >", modifier = Modifier.clickable {
                navController.navigate("Insights")
            })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("\u2B06\uFE0F Your Food Quality score", fontSize = 16.sp)
        Text("${score}/100", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF4CAF50))

        Spacer(modifier = Modifier.height(12.dp))

        Text("What is the Food Quality Score?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\n" +
                    "This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            fontSize = 14.sp
        )
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {
    val items = listOf("home", "Insights", "NutriCoach", "Settings")
    var selected by remember { mutableStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index
                    navController.navigate(label)
                },
                icon = {
                    val icon = when (label) {
                        "home" -> Icons.Default.Home
                        "Insights" -> Icons.Default.Info
                        "NutriCoach" -> Icons.Default.Person
                        else -> Icons.Default.Settings
                    }
                    Icon(icon, contentDescription = label)
                },
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun InsightsScreen(
    navController: NavHostController,
    viewModel: InsightsViewModel = viewModel(factory = InsightsViewModel.Factory(LocalContext.current))
)  {
    val context = LocalContext.current


    val categoryScores by viewModel.categoryScores.observeAsState(emptyList())
    val totalScore by viewModel.totalScore.observeAsState(0f)


    LaunchedEffect(Unit) {
        viewModel.loadInsights()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Insights: Food Score",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(categoryScores) { (label, score) ->
            val max = if (label in listOf("Water", "Alcohol")) 5f else 10f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, fontSize = 14.sp, modifier = Modifier.weight(1.5f))
                Slider(
                    value = score,
                    onValueChange = {},
                    valueRange = 0f..max,
                    enabled = false,
                    modifier = Modifier.weight(2f),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color(0xFF800080),
                        inactiveTrackColor = Color(0xFFCE93D8),
                        thumbColor = Color(0xFF800080),
                        disabledActiveTrackColor = Color(0xFF800080),
                        disabledInactiveTrackColor = Color(0xFFCE93D8),
                        disabledThumbColor = Color(0xFF800080)
                    )
                )
                Text(
                    "${"%.1f".format(score)}/$max",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Total Food Quality Score",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Slider(
                    value = totalScore,
                    onValueChange = {},
                    valueRange = 0f..100f,
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color(0xFF800080),
                        inactiveTrackColor = Color(0xFFCE93D8),
                        thumbColor = Color(0xFF800080),
                        disabledActiveTrackColor = Color(0xFF800080),
                        disabledInactiveTrackColor = Color(0xFFCE93D8),
                        disabledThumbColor = Color(0xFF800080)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${"%.1f".format(totalScore)}/100", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val context = LocalContext.current

                Button(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Hi, my Total Food Quality Score is ${"%.1f".format(totalScore)}/100!"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share your score via"))
                }) {
                    Text("Share with someone")
                }
                Button(onClick = {
                    navController.navigate("NutriCoach")
                }) {
                    Text("Improve my diet!")
                }
            }
        }
    }
}
@Composable
fun NutriCoachScreen(
    fruitViewModel: NutriCoachViewModel,
    aiViewModel: AIViewmodel
) {
    val context = LocalContext.current
    val fruit by fruitViewModel.fruit.observeAsState()
    val error by fruitViewModel.error.observeAsState()
    val fruitScore by fruitViewModel.fruitScore.observeAsState()
    val input by fruitViewModel.inputText.observeAsState("")
    val uiState by aiViewModel.uiState.observeAsState(UiState.Initial)
    val tips by aiViewModel.getTipsForUser().observeAsState(emptyList())
    var result by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fruitViewModel.loadFruitScore(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp),

    ) {
        Text(
            text = "Nutri Coach",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )

        if (fruitScore != null && fruitScore!! > 5f && fruitScore!! <= 10f) {
            Text("Fruit Name", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { fruitViewModel._inputText.value = it },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = { fruitViewModel.fetchFruitInfo(input) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                    Spacer(Modifier.width(4.dp))
                    Text("Details")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            fruit?.let {
                InfoRow("family", it.family)
                InfoRow("calories", it.nutritions.calories.toString())
                InfoRow("fat", it.nutritions.fat.toString())
                InfoRow("sugar", it.nutritions.sugar.toString())
                InfoRow("carbohydrates", it.nutritions.carbohydrates.toString())
                InfoRow("protein", it.nutritions.protein.toString())
            }
            error?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp)) }
        } else if (fruitScore != null) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/300"),
                contentDescription = "Random fruit image",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        GenAISection(viewModel = aiViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "Shows All Tips")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Done")
                    }
                },
                title = {
                    Text("AI Tips", fontWeight = FontWeight.Bold)
                },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 400.dp)
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tips) { tip ->
                            Text(
                                text = tip.message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun GenAISection(viewModel: AIViewmodel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.observeAsState(UiState.Initial)
    var result by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        Button(
            onClick = { viewModel.sendPrompt(context)},
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp)
        ) {
            Text("Motivational Message (AI)")
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            val textColor = when (uiState) {
                is UiState.Error -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurface
            }

            result = when (uiState) {
                is UiState.Success -> (uiState as UiState.Success).outputText
                is UiState.Error -> (uiState as UiState.Error).errorMessage
                else -> result
            }

            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("$label :", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(value, modifier = Modifier.weight(1f))
    }
}
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(LocalContext.current))) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
    val userId = prefs.getString("currentUserId", null)?.toIntOrNull()

    val patient by viewModel.patient.observeAsState()


    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadCurrentUser(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("ACCOUNT", fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        if (patient != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "User")
                Spacer(Modifier.width(12.dp))
                Text(patient!!.name ?: "Unknown", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Call, contentDescription = "Phone")
                Spacer(Modifier.width(12.dp))
                Text(patient!!.phoneNumber, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountBox, contentDescription = "User ID")
                Spacer(Modifier.width(12.dp))
                Text(patient!!.userId.toString(), fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text("OTHER SETTINGS", fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    prefs.edit().clear().apply()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                .padding(vertical = 12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            Spacer(Modifier.width(12.dp))
            Text("Logout", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startActivity(Intent(context, ClinicianLoginActivity::class.java))
                }
                .padding(vertical = 12.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Clinician")
            Spacer(Modifier.width(12.dp))
            Text("Clinician Login", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }
    }
}