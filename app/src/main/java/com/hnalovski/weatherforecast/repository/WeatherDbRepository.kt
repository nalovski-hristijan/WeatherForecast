package com.hnalovski.weatherforecast.repository

import com.hnalovski.weatherforecast.data.WeatherDao
import com.hnalovski.weatherforecast.data.WeatherDatabase
import com.hnalovski.weatherforecast.model.Favorite
import com.hnalovski.weatherforecast.model.Unit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {
    // Favorites
    fun getFavorites(): Flow<List<Favorite>> = weatherDao.getFavorites()

    suspend fun insertFavorite(favorite: Favorite) = weatherDao.insertFavCity(favorite)

    suspend fun updateFavorite(favorite: Favorite) = weatherDao.updateFav(favorite)

    suspend fun deleteFavorite(favorite: Favorite) = weatherDao.deleteFav(favorite)

    suspend fun deleteAllFavorites() = weatherDao.deleteAllFavorites()

    // Units

    fun getUnits(): Flow<List<Unit>> = weatherDao.getUnits()

    suspend fun insertUnit(unit: Unit) = weatherDao.insertUnit(unit)

    suspend fun updateUnit(unit: Unit) = weatherDao.updateUnit(unit)

    suspend fun deleteUnit(unit: Unit) = weatherDao.deleteUnit(unit)

    suspend fun deleteAllUnits() = weatherDao.deleteAllUnits()

}