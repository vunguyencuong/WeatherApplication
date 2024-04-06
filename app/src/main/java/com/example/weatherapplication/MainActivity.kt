package com.example.weatherapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.HourlyAdapter
import com.example.weatherapplication.Domain.Hourly
import com.example.weatherapplication.ViewModel.WeatherViewModel
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.model.CurrentResponseApi
import retrofit2.Call
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapterHourly: HourlyAdapter
    private val weatherViewModel = WeatherViewModel()
    private var lat =  34.052235
    private var lon = -118.243683


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
            object : retrofit2.Callback<CurrentResponseApi>{
                override fun onResponse(
                    p0: Call<CurrentResponseApi>,
                    p1: Response<CurrentResponseApi>
                ) {
                    if(p1.isSuccessful){
                        val data =p1.body()
                        data?.let {
                            binding.tvCityName.text = it.name.toString()
                            binding.tvStateWeather.text = it.weather?.get(0)?.main ?: "-"
                            binding.tvCurrentTemperature.text = it.main?.temp?.let { Math.round(it).toString() }+"°"
                            binding.tvHighAndLowTemperature.text =  "H:" + it.main?.tempMax?.let { Math.round(it).toString() } + "  " + "L:" + it.main?.tempMin?.let { Math.round(it).toString() }

                            val currentTime = it.dt?.toLong()

                            val sunriseTimestamp = it.sys?.sunrise?.toLong() ?: 0
                            val sunsetTimestamp = it.sys?.sunset?.toLong() ?: 0
                            val isDayTime  = currentTime in sunriseTimestamp until sunsetTimestamp
                            val drawableBackground  = if(isDayTime) R.drawable.night_bg
                            else{
                                setDynamicWallpaper(it.weather?.get(0)?.icon?:"-")
                            }
                            Log.d("background", "onResponse: $drawableBackground")
                            binding.background.setBackgroundResource(drawableBackground)


                        }
                    }
                }

                override fun onFailure(p0: Call<CurrentResponseApi>, p1: Throwable) {
                    Toast.makeText(this@MainActivity,"Error", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun setDynamicWallpaper(icon : String) : Int{
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