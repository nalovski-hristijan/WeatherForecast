package com.hnalovski.weatherforecast.screens.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hnalovski.weatherforecast.model.Unit
import com.hnalovski.weatherforecast.navigation.WeatherScreens
import com.hnalovski.weatherforecast.widget.WeatherAppBar

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    var unitToggleState by remember {
        mutableStateOf(false)
    }

    val measurementUnits = listOf("Metric (C)", "Imperial (F)")

    val choiceFromDb = settingsViewModel.unitList.collectAsState().value

    val defaultChoice =
        if (choiceFromDb.isEmpty()) measurementUnits[0] else choiceFromDb[0].unit

    var choiceState by remember {
        mutableStateOf(defaultChoice)
    }
    Scaffold(topBar = {
        WeatherAppBar(
            title = "Settings",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            isMainScreen = false,
            navController = navController,
            onButtonClicked = { navController.popBackStack() }
        )
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(1.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Units of Measurement",
                    style = MaterialTheme.typography.titleLarge
                )

                IconToggleButton(
                    checked = !unitToggleState, onCheckedChange = {
                        unitToggleState = !it
                        choiceState = if (unitToggleState) {
                            "Metric (C)"
                        } else {
                            "Imperial (F)"
                        }

                        Log.d("Units", "SettingsScreen: $it")
                    }, modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(shape = RectangleShape)
                        .padding(5.dp)
                        .background(color = Color.Magenta.copy(alpha = 0.4f))
                ) {
                    Text(text = if (unitToggleState) "Celsius °C" else "Fahrenheit °F")
                }

                Button(
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFBE42)
                    ),
                    onClick = {
                        settingsViewModel.deleteAllUnits()
                        settingsViewModel.insertUnit(Unit(unit = choiceState))
                        navController.popBackStack()
                    }) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(4.dp),
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }

            }
        }
    }
}