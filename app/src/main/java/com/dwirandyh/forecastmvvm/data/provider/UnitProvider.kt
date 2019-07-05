package com.dwirandyh.forecastmvvm.data.provider

import com.dwirandyh.forecastmvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}