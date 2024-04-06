package com.example.weatherapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapplication.Repository.WeatherRepository
import com.example.weatherapplication.Server.ApiClient
import com.example.weatherapplication.Server.ApiService

class WeatherViewModel(val repository : WeatherRepository) : ViewModel() {

    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiService::class.java)))

    fun loadCurrentWeather(lat: Double, lon : Double, units : String)=
        repository.getCurrentWeather(lat,lon,units)
}