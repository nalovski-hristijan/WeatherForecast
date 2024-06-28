package com.hnalovski.weatherforecast.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import coil.compose.rememberAsyncImagePainter
import com.hnalovski.weatherforecast.R
import com.hnalovski.weatherforecast.data.DataOrException
import com.hnalovski.weatherforecast.model.Weather
import com.hnalovski.weatherforecast.model.WeatherItem
import com.hnalovski.weatherforecast.utils.formatDate
import com.hnalovski.weatherforecast.utils.formatDateTime
import com.hnalovski.weatherforecast.utils.formatDecimals
import com.hnalovski.weatherforecast.widget.WeatherAppBar

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {

    val weatherData =
        produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(
                loading = true
            ), producer = {
                value = viewModel.getWeatherData(city = "Istanbul")
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

    }


}

@Composable
fun SunriseSunsetRow(weather: WeatherItem) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.padding(1.dp)) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.sunrise),
                contentDescription = "sunrise icon"
            )
            Text(text = formatDateTime(weather.sunrise))
        }
        Row(modifier = Modifier.padding(1.dp)) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.sunset),
                contentDescription = "sunrise icon"
            )
            Text(text = formatDateTime(weather.sunset))
        }
    }
}

@Composable
fun HumidityWindPressureRow(weather: WeatherItem) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.humidity),
                contentDescription = "humidity image",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = weather.humidity.toString() + "%",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.pressure),
                contentDescription = "pressure image",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = weather.pressure.toString() + " Pa",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.wind),
                contentDescription = "wind image",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = weather.gust.toString() + " kph",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun WeatherStateImage(imagesUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imagesUrl),
        contentDescription = "icon image",
        modifier = Modifier.size(80.dp)
    )
}




