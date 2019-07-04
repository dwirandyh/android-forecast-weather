package com.dwirandyh.forecastmvvm.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    @Embedded(prefix = "condition_")
    val condition: Condition,

    @SerializedName("feelslike_c")
    @ColumnInfo(name = "feelslikeC")
    val feelslikeC: Double,

    @SerializedName("feelslike_f")
    @ColumnInfo(name = "feelslikeF")
    val feelslikeF: Double,

    @SerializedName("gust_kph")
    @ColumnInfo(name = "gustKph")
    val gustKph: Double,

    @SerializedName("gust_mph")
    @ColumnInfo(name = "gustMph")
    val gustMph: Double,

    @SerializedName("is_day")
    @ColumnInfo(name = "isDay")
    val isDay: Int,

    @SerializedName("precip_in")
    @ColumnInfo(name = "precipIn")
    val precipIn: Double,

    @SerializedName("precip_mm")
    @ColumnInfo(name = "precipMm")
    val precipMm: Double,

    @SerializedName("temp_c")
    @ColumnInfo(name = "tempC")
    val tempC: Double,

    @SerializedName("temp_f")
    @ColumnInfo(name = "tempF")
    val tempF: Double,

    @ColumnInfo(name = "uv")
    val uv: Double,

    @SerializedName("vis_km")
    @ColumnInfo(name = "visKm")
    val visKm: Double,

    @SerializedName("vis_miles")
    @ColumnInfo(name = "visMiles")
    val visMiles: Double,

    @SerializedName("wind_dir")
    @ColumnInfo(name = "windDir")
    val windDir: String,

    @SerializedName("wind_kph")
    @ColumnInfo(name = "windKph")
    val windKph: Double,

    @SerializedName("wind_mph")
    @ColumnInfo(name = "windMph")
    val windMph: Double
){
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id : Int = CURRENT_WEATHER_ID // single row
}