package com.dwirandyh.forecastmvvm.ui.weather.future.list

import androidx.lifecycle.ViewModel;
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.internal.UnitSystem
import com.dwirandyh.forecastmvvm.internal.lazyDeffered
import com.dwirandyh.forecastmvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeffered{
        forecastRepository.getFutureWeatherList(LocalDate.now(), isMetricUnit)
    }
}
