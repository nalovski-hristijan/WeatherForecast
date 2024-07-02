package com.hnalovski.weatherforecast.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hnalovski.weatherforecast.model.Favorite
import com.hnalovski.weatherforecast.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: WeatherDbRepository) :
    ViewModel() {

    private val _favList = MutableStateFlow<List<Favorite>>(emptyList())
    val favList = _favList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavorites().distinctUntilChanged().collect { listOfFavs ->
                if (listOfFavs.isEmpty()) {
                    Log.d("Empty", "Empty favs: ")
                } else {
                    _favList.value = listOfFavs
                    Log.d("Favs", ":$favList ")
                }
            }
        }
    }

    fun insertFavorite(favorite: Favorite) =
        viewModelScope.launch { repository.insertFavorite(favorite) }

    fun updateFavorite(favorite: Favorite) =
        viewModelScope.launch { repository.updateFavorite(favorite) }

    fun deleteFavorite(favorite: Favorite) =
        viewModelScope.launch { repository.deleteFavorite(favorite) }

    fun deleteAllFavorites() =
        viewModelScope.launch { repository.deleteAllFavorites() }
}