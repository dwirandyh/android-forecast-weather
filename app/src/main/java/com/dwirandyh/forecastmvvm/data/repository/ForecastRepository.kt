package com.dwirandyh.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    // susped for coroutines
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
}