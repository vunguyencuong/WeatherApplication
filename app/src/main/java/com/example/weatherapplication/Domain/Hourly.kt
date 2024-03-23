package com.example.weatherapplication.Domain

class Hourly(
    private var hour : String,
    private var temp : Int,
    private var picPath : Int
) {
    fun getHour(): String {
        return hour
    }

    fun setHour(newHour: String) {
        hour = newHour
    }

    fun getTemp(): Int {
        return temp
    }

    fun setTemp(newTemp: Int) {
        temp = newTemp
    }

    fun getPicPath(): Int {
        return picPath
    }

    fun setPicPath(newPicPath: Int) {
        picPath = newPicPath
    }

}