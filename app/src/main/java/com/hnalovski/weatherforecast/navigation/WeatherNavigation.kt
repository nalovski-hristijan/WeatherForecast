package com.hnalovski.weatherforecast.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hnalovski.weatherforecast.screens.SplashScreen
import com.hnalovski.weatherforecast.screens.main.MainScreen
import com.hnalovski.weatherforecast.screens.main.MainViewModel
import com.hnalovski.weatherforecast.screens.search.SearchScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {

        composable(route = WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        val route = WeatherScreens.MainScreen.name
        composable("$route/{city}", arguments = listOf(
            navArgument(name = "city") {
                NavType.StringType
            }
        )) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                val viewModel = hiltViewModel<MainViewModel>()
                MainScreen(navController = navController, viewModel = viewModel, city = city)
            }

        }

        composable(route = WeatherScreens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }

    }
}