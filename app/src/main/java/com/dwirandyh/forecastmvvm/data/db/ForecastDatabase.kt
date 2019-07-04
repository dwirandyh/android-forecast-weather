package com.dwirandyh.forecastmvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dwirandyh.forecastmvvm.data.db.entity.CurrentWeatherEntry

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object{

    }
}