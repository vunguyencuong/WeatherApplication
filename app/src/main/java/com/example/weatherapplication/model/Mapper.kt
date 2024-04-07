package com.example.weatherapplication.model


fun CurrentResponsePojo.toCurrentResponse(): CurrentResponse {
    return CurrentResponse(
        name = name,
        weatherStatus = weather[0].main,
        currentTemperature = main.temp,
        tempHi = main.tempMax,
        tempLow = main.tempMin,
        currentTime = dt,
        sunriseTime = sys.sunrise,
        sunsetTime = sys.sunset,
        icon = weather[0].icon,
        percentRain = rain?.h,
        windSpeed = wind.speed,
        humidity = main.humidity,
        forecastWeathers = weather.drop(1).map {
            CurrentResponse.ForecastWeather(it.description,it.icon,it.main)
        })
}
