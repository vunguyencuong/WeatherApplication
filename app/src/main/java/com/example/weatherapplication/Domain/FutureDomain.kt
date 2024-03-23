package com.example.weatherapplication.Domain

class FutureDomain(
    private var day : String,
    private var picPath : String,
    private var status : String,
    private var highTemp : Int,
    private var lowTemp : Int
) {

    fun getDay(): String {
        return day
    }

    fun setDay(newDay: String) {
        day = newDay
    }

    fun getPicPath(): String {
        return picPath
    }

    fun setPicPath(newPicPath: String) {
        picPath = newPicPath
    }

    fun getStatus(): String {
        return status
    }

    fun setStatus(newStatus: String) {
        status = newStatus
    }

    fun getHighTemp(): Int {
        return highTemp
    }

    fun setHighTemp(newHighTemp: Int) {
        highTemp = newHighTemp
    }

    fun getLowTemp(): Int {
        return lowTemp
    }

    fun setLowTemp(newLowTemp: Int) {
        lowTemp = newLowTemp
    }

}