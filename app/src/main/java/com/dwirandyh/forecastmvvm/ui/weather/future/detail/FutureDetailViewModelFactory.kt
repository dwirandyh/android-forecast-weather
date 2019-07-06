package com.dwirandyh.forecastmvvm.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dwirandyh.forecastmvvm.data.provider.UnitProvider
import com.dwirandyh.forecastmvvm.data.repository.ForecastRepository
import org.threeten.bp.LocalDate

class FutureDetailViewModelFactory(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailViewModel(detailDate, forecastRepository, unitProvider) as T
    }
}