package com.example.weatherapplication.model

data class CurrentResponse(
    val name: String,
    val weatherStatus: String,
    val currentTemperature: Double,
    val tempHi: Double,
    val tempLow: Double,
    val currentTime: Int,
    val sunriseTime: Int,
    val sunsetTime: Int,
    val icon: String,
    val percentCloud : Int,
    val windSpeed : Double,
    val humidity : Int,
){
}