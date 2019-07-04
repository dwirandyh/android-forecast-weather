package com.dwirandyh.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.dwirandyh.forecastmvvm.internal.UnitSystem
import com.dwirandyh.forecastmvvm.internal.lazyDeffered
import com.dwirandyh.forecastmvvm.repository.ForecastRepository

class CurrentWeatherViewModel(
    private val forcastRepository: ForecastRepository
) : ViewModel() {

    private val unitSystem = UnitSystem.METRIC // get from settings later

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeffered {
        forcastRepository.getCurrentWeather(isMetric)
    }
}
