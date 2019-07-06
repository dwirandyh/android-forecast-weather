package com.dwirandyh.forecastmvvm.ui.weather.future.detail

import androidx.lifecycle.ViewModel;
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import com.dwirandyh.forecastmvvm.internal.lazyDeffered
import com.dwirandyh.forecastmvvm.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailViewModel(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeffered{
        forecastRepository.getFutureWeatherByDate(detailDate, isMetricUnit)
    }
}
