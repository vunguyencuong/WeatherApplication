package com.example.weatherapplication.Server

import com.example.weatherapplication.model.CityResponsePojo
import com.example.weatherapplication.model.CurrentResponsePojo
import com.example.weatherapplication.model.ForecastResponsePojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") ApiKey : String
    ) : Call<CurrentResponsePojo>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") ApiKey : String
    ) : Call<ForecastResponsePojo>

    @GET("geo/1.0/direct")
    fun getListCity(
        @Query("q") lat : String,
        @Query("limit") limit : Int,
        @Query("appid") ApiKey : String
    )  : Call<CityResponsePojo>
}