package com.example.weatherapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapplication.Repository.CityRepository
import com.example.weatherapplication.Repository.WeatherRepository
import com.example.weatherapplication.Server.ApiClient
import com.example.weatherapplication.Server.ApiService

class CityViewModel(val repository: CityRepository) : ViewModel() {

    constructor() : this(CityRepository(ApiClient().getClient().create(ApiService::class.java)))

    fun loadCity(q: String, limit: Int) =
        repository.getListCity(q,limit)
}