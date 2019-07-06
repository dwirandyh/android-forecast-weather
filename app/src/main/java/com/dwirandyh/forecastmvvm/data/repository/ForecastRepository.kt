package com.dwirandyh.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.dwirandyh.forecastmvvm.data.db.entity.WeatherLocation
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    // susped for coroutines
    // out is using when u usae interface for return value, out will you allow to return class that inherit from interface
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
    suspend fun getFutureWeatherByDate(date: LocalDate, metric: Boolean): LiveData<out UnitSpecificDetailFutureWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}