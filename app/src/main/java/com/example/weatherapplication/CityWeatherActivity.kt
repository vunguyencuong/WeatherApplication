package com.example.weatherapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.Adapter.CityAdapter
import com.example.weatherapplication.ViewModel.CityViewModel
import com.example.weatherapplication.databinding.ActivityCityWeatherBinding
import com.example.weatherapplication.model.CityResponsePojo
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityWeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityWeatherBinding
    private val cityViewModel = CityViewModel()
    private val adapterCity = CityAdapter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>> // Đổi kiểu biến này

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handleLocationPermissionResult(permissions)
        }
        setupViews()
    }

    private fun setupViews() {
        binding.imgLocation.setOnClickListener {
            requestLocation()
        }

        binding.etEnterNameCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.progressBarSearch.visibility = View.VISIBLE
                cityViewModel.loadCity(p0.toString(), 10).enqueue(object : Callback<CityResponsePojo> {
                    override fun onResponse(
                        call: Call<CityResponsePojo>,
                        response: Response<CityResponsePojo>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {
                                binding.progressBarSearch.visibility = View.GONE
                                adapterCity.differ.submitList(it)
                                binding.listCity.apply {
                                    layoutManager =
                                        LinearLayoutManager(this@CityWeatherActivity, LinearLayoutManager.HORIZONTAL, false)
                                    adapter = adapterCity
                                }
                            }
                        }
                    }

                    override fun onFailure(p0: Call<CityResponsePojo>, p1: Throwable) {
                    }
                })
            }
        })
    }

    private fun requestLocation() {
//        val locationPermissionRequest =
//            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//                when {
//                    it.getOrDefault(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        false
//                    ) || it.getOrDefault(
//                        Manifest.permission.ACCESS_COARSE_LOCATION, false
//                    ) -> {
//                        Toast.makeText(this, "Location access granted", Toast.LENGTH_SHORT).show()
//
//                        if (isLocationEnabled()) {
//                            fetchLocation()
//                        } else {
//                            Toast.makeText(this, "Please turn ON the location.", Toast.LENGTH_SHORT)
//                            createLocationRequest()
//                        }
//                    }
//                    else -> {
//                        Toast.makeText(this, "No location access", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun handleLocationPermissionResult(permissions: Map<String, Boolean>) {
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            // Quyền truy cập vị trí đã được cấp, tiến hành kiểm tra trạng thái vị trí và thực hiện hành động tương ứng
            if (isLocationEnabled()) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Please turn ON the location.", Toast.LENGTH_SHORT).show()
                createLocationRequest()
            }
        } else {
            // Quyền truy cập vị trí không được cấp, thực hiện hành động tương ứng
            Toast.makeText(this, "No location access", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        val result = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
        result.addOnCompleteListener {
            val lat = it.result.latitude
            val lon = it.result.longitude
            Log.d("get location", "onCreate: $lat $lon")
            val intent = Intent(this@CityWeatherActivity, MainActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            this@CityWeatherActivity.startActivity(intent)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000
        ).setMinUpdateIntervalMillis(5000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
        }

        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        this,
                        100
                    )
                } catch (sendEx: Exception) {
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle permissions result if needed
    }
}
