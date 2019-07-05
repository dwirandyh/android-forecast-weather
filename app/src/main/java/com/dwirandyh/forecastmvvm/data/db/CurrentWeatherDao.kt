package com.dwirandyh.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dwirandyh.forecastmvvm.data.db.entity.CURRENT_WEATHER_ID
import com.dwirandyh.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.ImperialCurrentWeatherEntry
import com.dwirandyh.forecastmvvm.data.db.unitlocalized.current.MetricCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    // if there is conflict couse of same primary key then replace that row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry) // update or insert

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeatherEntry>

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}