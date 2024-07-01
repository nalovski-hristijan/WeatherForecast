package com.hnalovski.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_tbl")
data class Favorite(
    @PrimaryKey
    @ColumnInfo
    val city: String,

    @ColumnInfo
    val country: String
)