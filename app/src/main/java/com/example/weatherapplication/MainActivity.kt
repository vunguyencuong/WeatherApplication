package com.example.weatherapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.HourlyAdapter
import com.example.weatherapplication.Domain.Hourly
import com.example.weatherapplication.ViewModel.WeatherViewModel
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.model.CurrentResponse
import com.example.weatherapplication.model.CurrentResponsePojo
import com.example.weatherapplication.model.toCurrentResponse
import retrofit2.Call
import retrofit2.Response
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapterHourly: HourlyAdapter
    private val weatherViewModel = WeatherViewModel()
    private var lat =  20.97136
    private var lon = 105.77876


    val listPicPath : List<Int> = listOf(
        R.drawable.cloudy,
        R.drawable.sunny,
        R.drawable.wind,
        R.drawable.rainy,
        R.drawable.storm
    )


    val listItem : List<Hourly> = listOf(
        Hourly("5 pm", 28,listPicPath[0]),
        Hourly("6 pm", 28,listPicPath[1]),
        Hourly("7 pm", 28,listPicPath[2]),
        Hourly("8 pm", 28,listPicPath[3]),
        Hourly("9 pm", 28,listPicPath[3]),
        Hourly("10 pm", 28,listPicPath[4]),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCurrentWeather()
        setupRecyclerView()
        onClickWeatherFuture()
    }


    fun getCurrentWeather(){
        weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(
            object : retrofit2.Callback<CurrentResponsePojo>{
                override fun onResponse(
                    p0: Call<CurrentResponsePojo>,
                    p1: Response<CurrentResponsePojo>
                ) {
                    if(p1.isSuccessful){
                        val data =p1.body()
                        data?.let {
                            val currentResponse = it.toCurrentResponse()
                            binding.tvCityName.text = currentResponse.name
                            binding.tvStateWeather.text = currentResponse.weatherStatus
                            binding.tvCurrentTemperature.text = getCurrentTemp(currentResponse)
                            binding.tvHighAndLowTemperature.text = String.format("H:%s  L:%s", getTempHi(currentResponse), getTempLow(currentResponse))
                            binding.tvPercentRain.text = getPercentRain(currentResponse)
                            binding.tvWindSpeed.text = getWindSpeed(currentResponse)
                            binding.tvHumidity.text = getHumidity(currentResponse)

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
        return round( it.tempLow).toString()
    }

    private fun getTempHi(it: CurrentResponse) : String{
        return round(it.tempHi).toString()
    }

    private fun getPercentRain(it : CurrentResponse) : String{
        return (it.percentRain?.toString() ?: "0") + "%"
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


    fun setupRecyclerView(){
        adapterHourly = HourlyAdapter(listItem)
        binding.recyclerViewToday.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = adapterHourly
        }
    }

    fun onClickWeatherFuture(){
        binding.tVNext7Day.setOnClickListener {
            val intent = Intent(this,FutureWeatherActivity::class.java)
            startActivity(intent)
        }
    }

}