package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.FutureAdapter
import com.example.weatherapplication.Domain.FutureDomain
import com.example.weatherapplication.databinding.ActivityFutureWeatherBinding

class FutureWeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFutureWeatherBinding

    private lateinit var adapterFutureDomain : FutureAdapter

    val listItem : List<FutureDomain> = listOf(
        FutureDomain("Sat",R.drawable.storm,"Storm",21,7),
        FutureDomain("Sun",R.drawable.cloudy,"Cloudy",22,8),
        FutureDomain("Mon",R.drawable.windy,"Windy",23,9),
        FutureDomain("Tue",R.drawable.cloudy_sunny,"Cloud Sunny",24,10),
        FutureDomain("Wen",R.drawable.sunny,"Sunny",25,11),
        FutureDomain("Thu",R.drawable.rainy,"Rainy",26,12)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFutureWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    fun setupRecyclerView(){
        adapterFutureDomain = FutureAdapter(listItem)
        binding.recyclerViewFuture.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = adapterFutureDomain
        }
    }
}