package com.dwirandyh.forecastmvvm.ui.base

import androidx.lifecycle.ViewModel
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.internal.UnitSystem
import com.dwirandyh.forecastmvvm.internal.lazyDeffered

abstract class WeatherViewModel(
    private val forcastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeffered {
        forcastRepository.getWeatherLocation()
    }
}