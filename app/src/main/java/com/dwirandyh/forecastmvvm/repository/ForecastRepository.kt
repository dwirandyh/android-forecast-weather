package com.dwirandyh.forecastmvvm.repository

interface ForecastRepository {
    // susped for coroutines
    suspend fun getCurrentWeather(metric: Boolean)
}