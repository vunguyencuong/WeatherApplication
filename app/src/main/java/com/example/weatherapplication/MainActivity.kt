package com.example.weatherapplication

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.HourlyAdapter
import com.example.weatherapplication.Domain.Hourly
import com.example.weatherapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapterHourly: HourlyAdapter

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
        setupRecyclerView()
        onClickWeatherFuture()
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