package com.example.weatherapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.Domain.FutureDomain
import com.example.weatherapplication.R

class FutureAdapter(
    private  val items : List<FutureDomain>
) : RecyclerView.Adapter<FutureAdapter.FutureViewHolder>(){
    class FutureViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvDay : TextView = itemView.findViewById(R.id.tv_day);
        val imgFutureWeather : ImageView = itemView.findViewById(R.id.img_future_weather)
        val tvFutureWeather : TextView = itemView.findViewById(R.id.tv_future_weather)
        val tvHighTemp : TextView = itemView.findViewById(R.id.tv_highest_temp)
        val tvLowTemp : TextView = itemView.findViewById(R.id.tv_lowest_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_future_weather,parent,false)
        return FutureViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FutureViewHolder, position: Int) {
        val itemFuture = items[position]
        holder.tvDay.text = itemFuture.getDay()
        holder.imgFutureWeather.setImageResource(itemFuture.getPicPath())
        holder.tvFutureWeather.text = itemFuture.getStatus()
        holder.tvHighTemp.text = itemFuture.getHighTemp().toString() + "°"
        holder.tvLowTemp.text = itemFuture.getLowTemp().toString() + "°"
    }


}