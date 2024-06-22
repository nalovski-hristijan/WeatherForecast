package com.hnalovski.weatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hnalovski.weatherforecast.screens.SplashScreen
import com.hnalovski.weatherforecast.screens.main.MainScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {

        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = WeatherScreens.MainScreen.name) {
            MainScreen(navController = navController)
        }

    }
}