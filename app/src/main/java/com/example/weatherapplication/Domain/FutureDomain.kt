package com.example.weatherapplication.Domain

class FutureDomain(
    private var day : String,
    private var picPath : Int,
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

    fun getPicPath(): Int {
        return picPath
    }

    fun setPicPath(newPicPath: Int) {
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