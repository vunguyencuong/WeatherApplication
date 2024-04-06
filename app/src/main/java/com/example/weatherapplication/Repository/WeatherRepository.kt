package com.example.weatherapplication.Repository

import com.example.weatherapplication.Server.ApiService

class WeatherRepository(val api : ApiService) {

    fun getCurrentWeather(lat: Double, lon : Double, units: String) =
        api.getCurrentWeather(lat,lon,units,"18a0246072f9d54106f5d5377dcbedd0")

}