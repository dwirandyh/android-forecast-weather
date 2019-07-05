package com.dwirandyh.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    // susped for coroutines
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}