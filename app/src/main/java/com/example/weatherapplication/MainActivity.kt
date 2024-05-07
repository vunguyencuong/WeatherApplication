package com.example.weatherapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.FutureAdapter
import com.example.weatherapplication.ViewModel.WeatherViewModel
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.model.CurrentResponse
import com.example.weatherapplication.model.CurrentResponsePojo
import com.example.weatherapplication.model.ForecastResponsePojo
import com.example.weatherapplication.model.toCurrentResponse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale
import kotlin.math.log
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val adapterWeather = FutureAdapter()
    private val weatherViewModel = WeatherViewModel()
    private var lat =  20.97136
    private var lon = 105.77876
    private var nameCity = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addNewCity()
        getCurrentWeather()
        getForecastWeather()
    }


    private fun getCurrentWeather(){
        Log.d("get location", "getCurrentWeather: $lat $lon")
        weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(
            object : Callback<CurrentResponsePojo>{
                override fun onResponse(
                    p0: Call<CurrentResponsePojo>,
                    p1: Response<CurrentResponsePojo>
                ) {
                    if(p1.isSuccessful){
                        val data =p1.body()
                        data?.let {
                            Log.d("now", "onResponse: $it")
                            val currentResponse = it.toCurrentResponse()
                            if(nameCity == "") binding.tvCityName.text = nameCity
                            else binding.tvCityName.text = currentResponse.name
                            binding.tvStateWeather.text = currentResponse.weatherStatus
                            binding.tvCurrentTemperature.text = getCurrentTemp(currentResponse)
                            binding.tvHighAndLowTemperature.text = String.format("H:%s  L:%s", getTempHi(currentResponse), getTempLow(currentResponse))
                            binding.tvPercentCloud.text = getCloudPercent(currentResponse)
                            binding.tvWindSpeed.text = getWindSpeed(currentResponse)
                            binding.tvHumidity.text = getHumidity(currentResponse)


                            binding.imgStateWeather.setImageResource(
                                when(it.weather.get(0).icon){
                                    "01d","0n" -> R.drawable.sunny
                                    "02d","02n","03d","03n" -> R.drawable.cloudy_sunny
                                    "04d","04n" -> R.drawable.cloudy
                                    "09d","09n", "10d","10n" -> R.drawable.rainy
                                    "11d","11n" -> R.drawable.storm
                                    "13d","13n" -> R.drawable.snowy
                                    "50d","50n" -> R.drawable.windy
                                    else -> R.drawable.sunny
                                }
                            )

                            val currentTime = currentResponse.currentTime

                            val sunriseTimestamp = currentResponse.sunriseTime
                            val sunsetTimestamp = currentResponse.sunsetTime
                            val isDayTime  = currentTime in sunriseTimestamp until sunsetTimestamp
                            val drawableBackground  = if(!isDayTime) R.drawable.night_bg
                            else{
                                getDynamicWallpaper(currentResponse.icon)
                            }
                            Log.d("background", "onResponse: $drawableBackground")
                            binding.background.setBackgroundResource(drawableBackground)


                        }
                    } else {
                        Log.d("background", "onResponse: not successful")
                    }
                }

                override fun onFailure(p0: Call<CurrentResponsePojo>, p1: Throwable) {
                    Toast.makeText(this@MainActivity,"Error", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun getCurrentTemp(currentResponse: CurrentResponse): String{
        return round(currentResponse.currentTemperature).toString() + "°"
    }
    private fun getTempLow(it: CurrentResponse) : String{
        return round( it.tempLow - 1).toString()
    }

    private fun getTempHi(it: CurrentResponse) : String{
        return round(it.tempHi+1).toString()
    }

    private fun getCloudPercent(it : CurrentResponse) : String{
        return it.percentCloud.toString()+"%"
    }

    private fun getWindSpeed(it : CurrentResponse) : String{
        return it.windSpeed.toString() + "km/h"
    }

    private fun getHumidity(it : CurrentResponse) : String{
        return it.humidity.toString() + "%"
    }

    private fun getDynamicWallpaper(icon : String) : Int{
        val check = icon.dropLast(1)
        Log.d("background", "setDynamicWallpaper: $check")
        return when(icon.dropLast(1)){
            "01" -> R.drawable.snow_bg
            "02","03","04"-> R.drawable.cloudy_bg
            "09","10","11" -> R.drawable.rainy_bg
            "13" -> R.drawable.snow_bg
            "50" -> R.drawable.snow_bg
            else -> 0
        }
    }


    private fun getForecastWeather(){
        weatherViewModel.loadForecastWeather(lat,lon,"metric")
            .enqueue(object : retrofit2.Callback<ForecastResponsePojo>{
                override fun onResponse(
                    p0: Call<ForecastResponsePojo>,
                    p1: Response<ForecastResponsePojo>
                ) {
                    if(p1.isSuccessful){
                        Log.d("forecast", "onResponse: success")
                        val data = p1.body()

                        data?.let {
                            Log.d("forecast", "onResponse: ${it.list}")
                            adapterWeather.differ.submitList(it.list)
                            binding.recyclerViewForecastWeather.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                                adapter = adapterWeather
                            }
                        }
                    }
                }

                override fun onFailure(p0: Call<ForecastResponsePojo>, p1: Throwable) {
                    Log.d("forecast", "onResponse: fail")
                }

            })
    }

    private fun addNewCity(){
        lat = intent.getDoubleExtra("lat",0.0)
        lon = intent.getDoubleExtra("lon",0.0)
        nameCity = intent.getStringExtra("name").toString()
        if(lat == 0.0){
            lat = 20.97136
            lon = 105.77876
        }
        binding.btnAddCity.setOnClickListener {
            startActivity(Intent(this,CityWeatherActivity::class.java))
        }
    }
}

