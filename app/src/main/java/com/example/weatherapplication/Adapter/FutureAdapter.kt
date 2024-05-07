package com.example.weatherapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.ForecastResponsePojo
import java.text.SimpleDateFormat
import java.util.Calendar

class FutureAdapter : RecyclerView.Adapter<FutureAdapter.FutureViewHolder>(){
    class FutureViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvDay : TextView = itemView.findViewById(R.id.tv_day);
        val imgFutureWeather : ImageView = itemView.findViewById(R.id.img_weather)
        val tvTime : TextView = itemView.findViewById(R.id.tv_hourly)
        val tvTemp : TextView = itemView.findViewById(R.id.tv_temp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_future_weather,parent,false)
        return FutureViewHolder(view)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FutureViewHolder, position: Int) {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeek = when(calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        holder.tvDay.text = dayOfWeek
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val amPm = if(hour < 12) "am" else "pm"
        holder.tvTime.text = String.format("%02d:00",hour)
        holder.tvTemp.text = differ.currentList[position].main?.temp?.let { Math.round(it) }.toString()+ "°"

        val icon = when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d","0n" -> "sunny"
            "02d","02n" -> "cloudy_sunny"
            "03d","03n" -> "cloudy_sunny"
            "04d","04n" -> "cloudy"
            "09d","09n" -> "rainy"
            "10d","10n" -> "rainy"
            "11d","11n" -> "storm"
            "13d","13n" -> "snowy"
            "50d","50n" -> "windy"
            else -> "sunny"
        }

        holder.imgFutureWeather.setImageResource(
            when(icon){
                "sunny" -> R.drawable.sunny
                "cloudy_sunny" -> R.drawable.cloudy_sunny
                "cloudy" -> R.drawable.cloudy
                "rainy" -> R.drawable.rainy
                "storm" -> R.drawable.storm
                "snowy" -> R.drawable.snowy
                "windy" -> R.drawable.windy
                else -> R.drawable.sunny
            }
        )
    }


    private  val differCallback = object : DiffUtil.ItemCallback<ForecastResponsePojo.data>(){
        override fun areItemsTheSame(
            oldItem: ForecastResponsePojo.data,
            newItem: ForecastResponsePojo.data
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponsePojo.data,
            newItem: ForecastResponsePojo.data
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

}