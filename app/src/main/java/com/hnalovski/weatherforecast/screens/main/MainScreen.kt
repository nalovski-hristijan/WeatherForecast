package com.hnalovski.weatherforecast.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hnalovski.weatherforecast.data.DataOrException
import com.hnalovski.weatherforecast.model.Weather
import com.hnalovski.weatherforecast.navigation.WeatherScreens
import com.hnalovski.weatherforecast.screens.settings.SettingsViewModel
import com.hnalovski.weatherforecast.utils.formatDate
import com.hnalovski.weatherforecast.utils.formatDecimals
import com.hnalovski.weatherforecast.widget.HumidityWindPressureRow
import com.hnalovski.weatherforecast.widget.SunriseSunsetRow
import com.hnalovski.weatherforecast.widget.WeatherAppBar
import com.hnalovski.weatherforecast.widget.WeatherDetailRow
import com.hnalovski.weatherforecast.widget.WeatherStateImage

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
) {

    val currCity: String = if (city.isNullOrBlank()) "Bitola" else city

    val unitFromDb = settingsViewModel.unitList.collectAsState().value

    var unit by remember {
        mutableStateOf("metric")
    }

    var isMetric by remember {
        mutableStateOf(false)
    }

    if (unitFromDb.isNotEmpty()) {
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isMetric = unit == "metric"


        val weatherData =
            produceState<DataOrException<Weather, Boolean, Exception>>(
                initialValue = DataOrException(
                    loading = true
                ), producer = {
                    value = viewModel.getWeatherData(city = currCity, units = unit)
                }).value


        if (weatherData.loading == true) {
            CircularProgressIndicator()
        } else if (weatherData.data != null) {
            MainScaffold(weather = weatherData.data!!, navController, isMetric = isMetric)
        }
    }

    Log.d("CIT", "MainScreen: $city")


}

@Composable
fun MainScaffold(weather: Weather, navController: NavController, isMetric: Boolean) {
    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + " ,${weather.city.country}",
            navController = navController,
            elevation = 5.dp,
            onAddAction = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            }
        )
    }) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            MainContent(data = weather, isMetric = isMetric)
        }
    }
}

@Composable
fun MainContent(data: Weather, isMetric: Boolean) {

    val weatherItem = data.list[0]
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

    val unitLetter = if (isMetric) "C" else "F"

    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatDate(weatherItem.dt),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )

        Surface(
            modifier = Modifier
                .padding(5.dp)
                .size(200.dp), shape = CircleShape, color = Color(0xFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imagesUrl = imageUrl)
                Text(
                    text = formatDecimals(weatherItem.temp.day) + "°$unitLetter",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = weatherItem.weather[0].main, fontStyle = FontStyle.Italic)
            }
        }
        HumidityWindPressureRow(weather = weatherItem, isMetric)
        HorizontalDivider(thickness = 2.dp)
        SunriseSunsetRow(weather = weatherItem)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "This week",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(14.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(2.dp), contentPadding = PaddingValues(1.dp)) {
                items(items = data.list) { item ->
                    WeatherDetailRow(item, unitLetter)
                }
            }
        }

    }


}
