package com.example.weatherapplication.Notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.ViewModel.WeatherViewModel
import com.example.weatherapplication.model.CurrentResponsePojo
import com.example.weatherapplication.model.toCurrentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class WeatherNotificationWorker(appContext : Context, params: WorkerParameters) : Worker(appContext,params) {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)
    private val weatherViewModel = WeatherViewModel()

    override fun doWork(): Result {

        Log.d("get notify", "doWork: have notify")

        val sharedPreferences = applicationContext.getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("lat", 0.0f).toDouble()
        val lon = sharedPreferences.getFloat("lon", 0.0f).toDouble()
        Log.d("get notify", "lat lon : $lat $lon")
        fetchWeatherInfo(lat, lon){weatherInfo ->
            Log.d("get notify", "doWork: $weatherInfo")
            sendNotification(weatherInfo)
        }
        return Result.success()
    }


    private fun fetchWeatherInfo(lat: Double, lon : Double, onComplete:(String)-> Unit) {
        var noti = "Thời tiết "
        weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(
            object : Callback<CurrentResponsePojo> {
                override fun onResponse(
                    p0: Call<CurrentResponsePojo>,
                    p1: Response<CurrentResponsePojo>
                ) {
                    if(p1.isSuccessful){
                        val data =p1.body()
                        data?.let {
                            Log.d("get notify", "onResponse: $it")
                            val currentResponse = it.toCurrentResponse()
                            noti += currentResponse.name + ": " + (currentResponse.currentTemperature - 2).toInt() + "-" + (currentResponse.currentTemperature + 2).toInt() + "°C"
                            Log.d("get notify", "onResponse: $noti")
                        }
                    } else {
                        Log.d("background", "onResponse: not successful")
                    }

                    onComplete(noti)
                }

                override fun onFailure(p0: Call<CurrentResponsePojo>, p1: Throwable) {

                }

            }
        )
    }

    private fun sendNotification(weatherInfo: String) {

        val intent = Intent(applicationContext,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,
            PendingIntent.FLAG_IMMUTABLE)
        // Tạo một notification channel (chỉ cần thực hiện một lần)
        createNotificationChannel(applicationContext)

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Weather Update")
            .setContentText(weatherInfo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        // Hiển thị thông báo
        if (ActivityCompat.checkSelfPermission(
                /* context = */ applicationContext,
                /* permission = */ Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManager.notify(notificationId, builder.build())
    }

    companion object{
        private const val CHANNEL_ID = "Weather_Channel"
        private const val notificationId = 101

        fun createNotificationChannel(context: Context){
            val name = "Weather Notification"
            val descriptionText = "Notification for weather updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        fun scheduleWeatherNotification(localTime : LocalTime){
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val now = ZonedDateTime.now()
            val trigger = now.with(localTime)
            val realTrigger = when{
                trigger <= now -> trigger.plusDays(1)
                else -> trigger
            }
            val initalDelay = maxOf(1,realTrigger.toEpochSecond()-Instant.now().epochSecond)
            val request = PeriodicWorkRequest.Builder(WeatherNotificationWorker::class.java,1,TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(initalDelay,TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                "WeatherNotification",
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }
    }

}