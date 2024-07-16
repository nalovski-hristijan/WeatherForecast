package com.hnalovski.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_tbl")
data class Unit(
    @PrimaryKey
    @ColumnInfo
    val unit: String
)