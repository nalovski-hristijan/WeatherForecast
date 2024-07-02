package com.hnalovski.weatherforecast.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hnalovski.weatherforecast.model.Favorite
import com.hnalovski.weatherforecast.navigation.WeatherScreens
import com.hnalovski.weatherforecast.screens.favorites.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WeatherAppBar(
    title: String = "Title",
    icon: ImageVector? = null,
    navController: NavController = NavController(LocalContext.current),
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    onAddAction: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {

    val showDialog = remember {
        mutableStateOf(false)
    }

    if (showDialog.value) {
        ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
    }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
        },
        modifier = Modifier
            .shadow(elevation = elevation),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Arrow Back",
                    tint = Color.Black,
                    modifier = Modifier.clickable { onButtonClicked.invoke() }
                )
            }

            if (isMainScreen) {

                val isAlreadyFavList =
                    favoritesViewModel.favList.collectAsState().value.filter { item ->
                        (item.city == title.split(",")[0])

                    }

                val context = LocalContext.current

                if (isAlreadyFavList.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "favorite icon",
                        tint = Color.Red.copy(0.7f),
                        modifier = Modifier
                            .scale(0.9f)
                            .clickable {
                                favoritesViewModel.insertFavorite(
                                    Favorite(
                                        city = title.split(",")[0],
                                        country = title.split(",")[1]
                                    )
                                )

                                Toast.makeText(
                                    context,
                                    "City has been added to favorites",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    )
                } else {
                    Box {

                    }
                }
            }
        }, actions = {
            if (isMainScreen) {
                IconButton(onClick = { onAddAction.invoke() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                }

                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "More button"
                    )

                }
            } else {
                Box {

                }
            }
        })
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController) {

    var expanded by remember {
        mutableStateOf(true)
    }

    val items = listOf("About", "Favorites", "Settings")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(text = {
                    Text(text = text, modifier = Modifier.clickable {
                        navController.navigate(
                            when (text) {
                                "About" -> WeatherScreens.AboutScreen.name
                                "Favorites" -> WeatherScreens.FavoriteScreen.name
                                else -> WeatherScreens.SettingsScreen.name
                            }
                        )
                    }, fontWeight = FontWeight.W300)
                }, onClick = {
                    expanded = false
                    showDialog.value = false
                }, leadingIcon = {
                    Icon(
                        imageVector = when (text) {
                            "About" -> Icons.Default.Info
                            "Favorites" -> Icons.Default.Favorite
                            "Settings" -> Icons.Default.Settings
                            else -> Icons.Default.Settings
                        }, contentDescription = null, tint = Color.LightGray
                    )
                })
            }
        }
    }
}
