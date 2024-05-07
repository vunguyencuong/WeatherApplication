package com.example.weatherapplication.model

data class ForecastResponse(

    val weatherStatus : String,
    val currentTemperature: Double,
    val tempHi: Double,
    val tempLow: Double,
    val currentTime: Int,
    val icon: String,
    val percentRain : Double?,
    val windSpeed : Double,
    val humidity : Int,
)