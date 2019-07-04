package com.dwirandyh.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}