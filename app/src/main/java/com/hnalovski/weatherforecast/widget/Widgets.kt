package com.hnalovski.weatherforecast.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hnalovski.weatherforecast.R
import com.hnalovski.weatherforecast.model.WeatherItem
import com.hnalovski.weatherforecast.utils.formatDate
import com.hnalovski.weatherforecast.utils.formatDateTime
import com.hnalovski.weatherforecast.utils.formatDecimals


@Composable
fun WeatherDetailRow(weather: WeatherItem, unitLetter: String) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"

    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape.copy(topEnd = CornerSize(6.dp)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDate(weather.dt).split(",")[0],
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 5.dp)
            )

            WeatherStateImage(imagesUrl = imageUrl)

            Surface(
                shape = CircleShape,
                color = Color(0xFFFFC400)
            ) {
                Text(
                    text = weather.weather[0].main,
                    modifier = Modifier.padding(7.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue.copy(0.7f),
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(formatDecimals(weather.temp.max) + "°$unitLetter")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.LightGray
                    )
                ) {
                    append(formatDecimals(weather.temp.min) + "°$unitLetter")
                }
            }, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun SunriseSunsetRow(weather: WeatherItem) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp, start = 5.dp)
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
            Text(text = formatDateTime(weather.sunset))
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.sunset),
                contentDescription = "sunrise icon"
            )
        }
    }
}

@Composable
fun HumidityWindPressureRow(weather: WeatherItem, isMetric: Boolean) {

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
                text = weather.pressure.toString() + if (isMetric) " Pa" else " psi",
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
                text = weather.gust.toString() + if (isMetric) " m/s" else " mph",
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




