package com.dwirandyh.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel;
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.internal.UnitSystem
import com.dwirandyh.forecastmvvm.internal.lazyDeffered
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forcastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forcastRepository, unitProvider) {

    val weather by lazyDeffered {
        forcastRepository.getCurrentWeather(isMetricUnit)
    }


}
