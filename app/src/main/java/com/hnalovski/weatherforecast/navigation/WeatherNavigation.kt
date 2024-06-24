package com.hnalovski.weatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hnalovski.weatherforecast.screens.SplashScreen
import com.hnalovski.weatherforecast.screens.main.MainScreen
import com.hnalovski.weatherforecast.screens.main.MainViewModel

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {

        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = WeatherScreens.MainScreen.name) {
            val viewModel = hiltViewModel<MainViewModel>()
            MainScreen(navController = navController, viewModel = viewModel)
        }

    }
}