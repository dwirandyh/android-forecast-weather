package com.dwirandyh.forecastmvvm.data.provider

import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Jakarta"
    }
}