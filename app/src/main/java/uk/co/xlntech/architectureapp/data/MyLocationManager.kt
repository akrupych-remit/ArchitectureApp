package uk.co.xlntech.architectureapp.data

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*

class MyLocationManager(private val context: Context) : LifecycleObserver {

    val locationLiveData = MutableLiveData<Location>()

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun prepare() {
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 60000
            fastestInterval = 1000
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationLiveData.value = locationResult?.lastLocation
            }
        }
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTracking() {
        if (hasPermission()) locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        else locationLiveData.value = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTracking() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    private fun hasPermission(): Boolean = ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}