package com.example.weatherapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.Adapter.HourlyAdapter.ViewHolder
import com.example.weatherapplication.Domain.Hourly
import com.example.weatherapplication.R

class HourlyAdapter(
    private var items  : List<Hourly>
)  : RecyclerView.Adapter<ViewHolder>(){
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvHourly : TextView = itemView.findViewById(R.id.tv_hourly);
        val imgPic : ImageView = itemView.findViewById(R.id.img_weather_hourly);
        val tvHourlyTemp : TextView = itemView.findViewById(R.id.tv_hourly_temp);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemHourly = items[position]
        holder.tvHourly.text = itemHourly.getHour()
        holder.imgPic.setImageResource(itemHourly.getPicPath())
        holder.tvHourlyTemp.text = itemHourly.getTemp().toString() + "°"
    }

    override fun getItemCount(): Int {
        return items.size
    }


}