package com.example.weatherapplication.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.model.CityResponsePojo

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    class CityViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            val tvCityName : TextView = itemView.findViewById(R.id.tv_city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city,parent,false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.tvCityName.text = differ.currentList[position].name
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MainActivity::class.java)
            intent.putExtra("lat",differ.currentList[position].lat)
            intent.putExtra("lon",differ.currentList[position].lon)
            intent.putExtra("name",differ.currentList[position].name)
            holder.itemView.context.startActivity(intent)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CityResponsePojo.CityResponsePojoItem>(){
        override fun areItemsTheSame(
            oldItem: CityResponsePojo.CityResponsePojoItem,
            newItem: CityResponsePojo.CityResponsePojoItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CityResponsePojo.CityResponsePojoItem,
            newItem: CityResponsePojo.CityResponsePojoItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
}