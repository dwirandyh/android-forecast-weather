package com.dwirandyh.forecastmvvm.data.network.response


import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val forecast: ForecastDaysContainer,
    val location: WeatherLocation
)