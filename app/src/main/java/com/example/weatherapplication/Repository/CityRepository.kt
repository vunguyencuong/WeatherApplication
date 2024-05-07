package com.example.weatherapplication.Repository

import com.example.weatherapplication.Server.ApiService

class CityRepository(val api : ApiService) {

    fun getListCity(q : String, limit : Int) =
        api.getListCity(q,limit,"18a0246072f9d54106f5d5377dcbedd0")

}