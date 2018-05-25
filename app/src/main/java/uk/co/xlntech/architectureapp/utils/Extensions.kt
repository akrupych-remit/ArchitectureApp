package uk.co.xlntech.architectureapp.utils

import android.location.Location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

fun <T> Call<T>.enqueue(onResponse: (T) -> Unit, onFailure: (Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful && response.body() != null) onResponse(response.body()!!)
            else onFailure(Exception(response.errorBody()?.string()))
        }
        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(t)
        }
    })
}

operator fun Location?.component1() = this?.latitude

operator fun Location?.component2() = this?.longitude