package com.dwirandyh.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.internal.UnitSystem
import com.dwirandyh.forecastmvvm.internal.lazyDeffered
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository

class CurrentWeatherViewModel(
    private val forcastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeffered {
        forcastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeffered {
        forcastRepository.getWeatherLocation()
    }
}
