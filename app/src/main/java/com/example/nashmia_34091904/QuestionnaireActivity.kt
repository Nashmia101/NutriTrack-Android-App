package com.example.nashmia_34091904

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nashmia_34091904.ui.theme.Nashmia_34091904Theme
import java.util.Calendar
import com.example.nashmia_34091904.data.PatientRepository.QuestionnaireRepository
import com.example.nashmia_34091904.data.viewmodel.QuestionnaireViewModel
import com.example.nashmia_34091904.data.viewmodel.QuestionnaireViewModelFactory

class QuestionnaireActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nashmia_34091904Theme {
                val onBack = this.onBackPressedDispatcher
                val repository = QuestionnaireRepository(applicationContext)
                val factory = QuestionnaireViewModelFactory(repository)
                val viewModel: QuestionnaireViewModel = viewModel(factory = factory)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Food Intake Questionnaire", fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = { onBack.onBackPressed() }) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                ) { padding ->
                    QuestionnaireScreen(
                        viewModel,
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun timePickerFun(initial: String, onTimeSelected: (String) -> Unit): TimePickerDialog {
    val ctx = LocalContext.current
    val cal = Calendar.getInstance()
    return TimePickerDialog(
        ctx,
        { _, h, m -> onTimeSelected(String.format("%02d:%02d", h, m)) },
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        false
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(viewModel: QuestionnaireViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedPersona by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )

    val checked1 by viewModel.checkedStates[0].observeAsState(false)
    val checked2 by viewModel.checkedStates[1].observeAsState(false)
    val checked3 by viewModel.checkedStates[2].observeAsState(false)
    val checked4 by viewModel.checkedStates[3].observeAsState(false)
    val checked5 by viewModel.checkedStates[4].observeAsState(false)
    val checked6 by viewModel.checkedStates[5].observeAsState(false)
    val checked7 by viewModel.checkedStates[6].observeAsState(false)
    val checked8 by viewModel.checkedStates[7].observeAsState(false)
    val checked9 by viewModel.checkedStates[8].observeAsState(false)

    val persona by viewModel.persona.observeAsState("")
    val bigTime by viewModel.biggestMealTime.observeAsState("")
    val sleepTime by viewModel.sleepTime.observeAsState("")
    val morningTime by viewModel.wakeUpTime.observeAsState("")

    val bigTimeDialog = timePickerFun(bigTime) { viewModel.setBiggestMealTime(it) }
    val sleepTimeDialog = timePickerFun(sleepTime) { viewModel.setSleepTime(it) }
    val morningTimeDialog = timePickerFun(morningTime) { viewModel.setWakeUpTime(it) }

    Column(
        modifier = modifier
            .background(Color(0xFFADD8E6))
            .padding(horizontal = 16.dp)
            .padding(top = 1.dp),
        horizontalAlignment = Alignment.Start
    ) {


            Text(
                "Tick all the food categories you can eat",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked1, onCheckedChange = { viewModel.setChecked(1, it) })
                    Text(
                        "Fruits",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked2, onCheckedChange = { viewModel.setChecked(2, it) })
                    Text(
                        "Vegetables",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked3, onCheckedChange = { viewModel.setChecked(3, it) })
                    Text(
                        "Grains",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked4, onCheckedChange = { viewModel.setChecked(4, it) })
                    Text(
                        "Red Meat",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked5, onCheckedChange = { viewModel.setChecked(5, it) })
                    Text(
                        "Seafood",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked6, onCheckedChange = { viewModel.setChecked(6, it) })
                    Text(
                        "Poultry",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked7, onCheckedChange = { viewModel.setChecked(7, it) })
                    Text(
                        "Fish",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked8, onCheckedChange = { viewModel.setChecked(8, it) })
                    Text(
                        "Eggs",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Checkbox(checked = checked9, onCheckedChange = { viewModel.setChecked(9, it) })
                    Text(
                        "Nuts/Seeds",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(2.dp))

            Text("Your Persona", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(
                "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                PersonaButton("Health Devotee") { selectedPersona = it; showDialog = true }
                PersonaButton("Mindful Eater") { selectedPersona = it; showDialog = true }
                PersonaButton("Wellness Striver") { selectedPersona = it; showDialog = true }
            }
            Spacer(Modifier.height(2.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                PersonaButton("Balance Seeker") { selectedPersona = it; showDialog = true }
                PersonaButton("Health Procrastinator") { selectedPersona = it; showDialog = true }
                PersonaButton("Food Carefree") { selectedPersona = it; showDialog = true }
            }

            Spacer(Modifier.height(2.dp))



            Text("Which persona best fits you?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = persona,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = "Select option",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    options.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt) },
                            onClick = {
                                viewModel.setPersona(opt)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(2.dp))
            Text("Timings", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))


            @Composable
            fun TimeRow(label: String, time: String, onClick: () -> Unit) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = if (time.isNotEmpty()) time else "00:00",
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        textStyle = LocalTextStyle.current.copy(
                            fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .width(120.dp)
                            .clickable { onClick() }
                        )
                }
            }

            TimeRow(
                "What time of day approx. do you normally eat your biggest meal?",
                bigTime
            ) { bigTimeDialog.show() }
            TimeRow(
                "What time of day approx. do you go to sleep at night?",
                sleepTime
            ) { sleepTimeDialog.show() }
            TimeRow(
                "What time of day approx. do you wake up in the morning?",
                morningTime
            ) { morningTimeDialog.show() }

            Spacer(Modifier.height(2.dp))




            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.submitResponses()
                        context.startActivity(Intent(context, ScoreActivity::class.java))
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = "Save",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 8.sp
                    )
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(selectedPersona, fontWeight = FontWeight.Bold) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when (selectedPersona) {
                                "Health Devotee" -> {
                                    Text("I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.")
                                    Image(
                                        painterResource(id = R.drawable.health),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }

                                "Mindful Eater" -> {
                                    Text("I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.")
                                    Image(
                                        painterResource(id = R.drawable.mindfull),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }

                                "Wellness Striver" -> {
                                    Text("I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.")
                                    Image(
                                        painterResource(id = R.drawable.wellness_1),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }

                                "Balance Seeker" -> {
                                    Text("I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.")
                                    Image(
                                        painterResource(id = R.drawable.balance),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }

                                "Health Procrastinator" -> {
                                    Text("I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.")
                                    Image(
                                        painterResource(id = R.drawable.health2),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }

                                "Food Carefree" -> {
                                    Text("I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat.")
                                    Image(
                                        painterResource(id = R.drawable.food),
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) { Text("Dismiss") }
                    }
                )
            }
        }
    }

    @Composable
    fun PersonaButton(label: String, onClick: (String) -> Unit) {
        Button(
            onClick = { onClick(label) },
            modifier = Modifier
                .height(40.dp),
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text(
                text = label,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

