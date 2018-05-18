package uk.co.xlntech.architectureapp.data

import android.arch.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.xlntech.architectureapp.data.database.WorldLobbyDatabase
import uk.co.xlntech.architectureapp.data.entities.FeedPage
import uk.co.xlntech.architectureapp.data.entities.TipSummary
import uk.co.xlntech.architectureapp.utils.SingleLiveEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository(
        private val api: WorldLobbyApi,
        private val db: WorldLobbyDatabase,
        private val pageSize: Int = 50
) {
    val errors = SingleLiveEvent<Throwable>()

    private val dbExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun loadFeed(): LiveData<List<TipSummary>> {
        // start loading new data
        api.getFeed(skip = 0, limit = pageSize).enqueue(object : Callback<FeedPage> {
            override fun onResponse(call: Call<FeedPage>, response: Response<FeedPage>) {
                if (response.isSuccessful) saveFeed(response.body()!!.data)
                else errors.value = Exception(response.errorBody().toString())
            }
            override fun onFailure(call: Call<FeedPage>?, t: Throwable) {
                errors.value = t
            }
        })
        // return available items immediately
        return db.tipsDao().getTips()
    }

    private fun saveFeed(items: List<TipSummary>) {
        // observers will be triggered automatically
        dbExecutor.submit {
            db.runInTransaction {
                db.tipsDao().clearTips()
                db.tipsDao().insertTips(items)
            }
        }
    }
}