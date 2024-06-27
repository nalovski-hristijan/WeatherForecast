package com.hnalovski.weatherforecast.screens.main

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hnalovski.weatherforecast.data.DataOrException
import com.hnalovski.weatherforecast.model.Weather
import com.hnalovski.weatherforecast.widget.WeatherAppBar

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {

    val weatherData =
        produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            ), producer = {
                value = viewModel.getWeatherData(city = "Seattle")
            }).value


    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null) {
        MainScaffold(weather = weatherData.data!!, navController)
    }

}

@Composable
fun MainScaffold(weather: Weather, navController: NavController) {
    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + " ,${weather.city.country}",
            navController = navController,
            elevation = 5.dp,
            icon = Icons.AutoMirrored.Filled.ArrowBack
        ) {
            Log.d("Button", "MainScaffold: ButtonClicked")
        }
    }) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            MainContent(data = weather)
        }
    }
}

@Composable
fun MainContent(data: Weather) {
    Text(text = data.city.toString())
}




