package uk.co.xlntech.architectureapp.data

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
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
) : PagedList.BoundaryCallback<TipSummary>() {

    val feed: LiveData<PagedList<TipSummary>> =
            LivePagedListBuilder(db.tipsDao().getTips(), pageSize)
                    .setBoundaryCallback(this)
                    .build()
    val errors = SingleLiveEvent<Throwable>()

    private var offset = 0
    private var isLoading = false
    private val dbExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        loadFeed(0)
    }

    override fun onZeroItemsLoaded() {
        loadFeed(0)
    }

    override fun onItemAtEndLoaded(itemAtEnd: TipSummary) {
        offset += pageSize
        loadFeed(offset)
    }

    private fun loadFeed(offset: Int) {
        if (isLoading) return // BoundaryCallback can call this multiple times for single list
        isLoading = true
        api.getFeed(skip = offset, limit = pageSize).enqueue(object : Callback<FeedPage> {
            override fun onResponse(call: Call<FeedPage>, response: Response<FeedPage>) {
                if (response.isSuccessful) saveFeed(response.body()!!.data, offset == 0)
                else errors.value = Exception(response.errorBody().toString())
                isLoading = false
            }
            override fun onFailure(call: Call<FeedPage>?, t: Throwable) {
                errors.value = t
                isLoading = false
            }
        })
    }

    private fun saveFeed(items: List<TipSummary>, clear: Boolean) {
        // observers will be triggered automatically
        dbExecutor.submit {
            db.runInTransaction {
                if (clear) db.tipsDao().clearTips()
                db.tipsDao().insertTips(items)
            }
        }
    }
}