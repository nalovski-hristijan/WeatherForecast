package com.hnalovski.weatherforecast.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnalovski.weatherforecast.data.DataOrException
import com.hnalovski.weatherforecast.model.Weather
import com.hnalovski.weatherforecast.model.WeatherItem
import com.hnalovski.weatherforecast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun getWeatherData(city: String): DataOrException<Weather,Boolean,Exception> {
        return repository.getWeather(cityQuery = city, units = "metric")
    }

}