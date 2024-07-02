package com.hnalovski.weatherforecast.screens.favorites

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hnalovski.weatherforecast.model.Favorite
import com.hnalovski.weatherforecast.navigation.WeatherScreens
import com.hnalovski.weatherforecast.widget.WeatherAppBar


@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel
) {

    val list = viewModel.favList.collectAsState().value

    Scaffold(topBar = {
        WeatherAppBar(
            title = "Favorites",
            isMainScreen = false,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(5.dp)
            ) {

                items(items = list) {
                    FavoritesRow(
                        favorites = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesRow(
    favorites: Favorite,
    navController: NavController,
    viewModel: FavoritesViewModel
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(5.dp)
            .clickable {
                navController.navigate(WeatherScreens.MainScreen.name + "/${favorites.city}")
            }, shape = CircleShape.copy(topEnd = CornerSize(6.dp)), color = Color(0xFFB2DFDB)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = favorites.city, modifier = Modifier.padding(start = 4.dp))

            Surface(shape = CircleShape, color = Color(0xFFD1E3E1)) {
                Text(
                    text = favorites.country,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "delete icon",
                tint = Color.Red.copy(alpha = 0.3f),
                modifier = Modifier.clickable {
                    viewModel.deleteFavorite(favorites)
                    Toast.makeText(
                        context,
                        "${favorites.city} has been deleted from favorites",
                        Toast.LENGTH_LONG
                    ).show()
                })
        }
    }
}


