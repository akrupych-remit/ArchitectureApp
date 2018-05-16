package uk.co.xlntech.architectureapp.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.xlntech.architectureapp.data.entities.FeedPage
import uk.co.xlntech.architectureapp.data.entities.TipSummary

class MainRepository(
        private val api: WorldLobbyApi,
        private val pageSize: Int = 50
) {
    fun loadFeed(): LiveData<Result> {
        val liveData = MutableLiveData<Result>()
        api.getFeed(skip = 0, limit = pageSize).enqueue(object : Callback<FeedPage> {
            override fun onResponse(call: Call<FeedPage>, response: Response<FeedPage>) {
                if (response.isSuccessful) liveData.value = Success(response.body()!!.data)
                else liveData.value = Failure(response.message())
            }
            override fun onFailure(call: Call<FeedPage>?, t: Throwable) {
                liveData.value = Failure(t.message ?: "Unknown error")
            }
        })
        return liveData
    }
}

sealed class Result

data class Success(val items: List<TipSummary>) : Result()

data class Failure(val message: String) : Result()