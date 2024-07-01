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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
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
    city: String?
) {

    Log.d("CIT", "MainScreen: $city")

    val weatherData =
        produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            ), producer = {
                value = viewModel.getWeatherData(city = city.toString())
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
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            onAddAction = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            }
        )
    }) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            MainContent(data = weather)
        }
    }
}

@Composable
fun MainContent(data: Weather) {

    val weatherItem = data.list[0]
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

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
                    text = formatDecimals(weatherItem.temp.day) + "°C",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = weatherItem.weather[0].main, fontStyle = FontStyle.Italic)
            }
        }
        HumidityWindPressureRow(weather = weatherItem)
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
                    WeatherDetailRow(item)
                }
            }
        }

    }


}
