package com.dwirandyh.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.db.CurrentWeatherDao
import com.dwirandyh.forecastmvvm.data.db.FutureWeatherDao
import com.dwirandyh.forecastmvvm.data.db.WeatherLocationDao
import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.dwirandyh.forecastmvvm.data.network.FORECAST_DAYS_COUNT
import com.dwirandyh.forecastmvvm.data.network.WeatherNetworkDataSource
import com.dwirandyh.forecastmvvm.data.network.response.CurrentWeatherResponse
import com.dwirandyh.forecastmvvm.data.network.response.FutureWeatherResponse
import com.dwirandyh.forecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.LocalDate
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        // observe data then save data into local database
        weatherNetworkDataSource.apply {
            downloadCurrentWeather.observeForever { newCurrentWeather ->
                persisFectedCurrentWeather(newCurrentWeather)
            }

            downloadFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
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


    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeahterData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitSpecificDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeahterData()
            return@withContext if (metric) futureWeatherDao.getDetailWeatherByDateMetric(date)
            else futureWeatherDao.getDetailWeatherByDateImperial(date)
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

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {
        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeahterData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
        fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        val location = locationProvider.getPreferredLocationString()
        weatherNetworkDataSource.fetchCurrentWeather(
            location,
            Locale.getDefault().language
        )
    }

    private suspend fun fetchFutureWeather() {
        val location = locationProvider.getPreferredLocationString()
        weatherNetworkDataSource.fetchFutureWeather(
            location,
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFechTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFechTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }
}