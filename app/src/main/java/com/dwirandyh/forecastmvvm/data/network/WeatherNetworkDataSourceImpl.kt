package com.dwirandyh.forecastmvvm.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dwirandyh.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.dwirandyh.forecastmvvm.data.network.response.FutureWeatherResponse
import com.dwirandyh.forecastmvvm.internal.NoConnectivityException


const val FORECAST_DAYS_COUNT = 7

class WeatherNetworkDataSourceImpl(
    private val apixWeahterAPiService: ApixWeahterAPiService
) : WeatherNetworkDataSource {

    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = apixWeahterAPiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(location: String, languageCode: String) {
        try {
            val fetchedFutureWeather = apixWeahterAPiService
                .getFutureWeather(location, FORECAST_DAYS_COUNT, languageCode)
                .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}