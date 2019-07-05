package com.dwirandyh.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.db.CurrentWeatherDao
import com.dwirandyh.forecastmvvm.data.db.WeatherLocationDao
import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dwirandyh.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.dwirandyh.forecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        // observe data then save data into local database
        weatherNetworkDataSource.downloadCurrentWeather.observeForever { newCurrentWeather ->
            persisFectedCurrentWeather(newCurrentWeather)
        }
    }

    // "OUT" is for enable return class that implement interface, not only interface itself
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        // with context return value
        return withContext(Dispatchers.IO) {
            initWeahterData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric() else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persisFectedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        // use global scope is good because this class don't have lifecycle, but dont use it in fragment, because when fragment is destroyed
        // and coroutine finished is lete then you are going to get an exception
        // launch not return value
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeahterData() {
        // value is used becaouse getLocation() return liveData not value in LiveData
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        val location = locationProvider.getPreferredLocationString()
        weatherNetworkDataSource.fetchCurrentWeather(
            location,
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFechTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFechTime.isBefore(thirtyMinutesAgo)
    }
}