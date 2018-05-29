package uk.co.xlntech.architectureapp.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.location.Location
import android.util.Log
import uk.co.xlntech.architectureapp.data.api.TipsPagedDataSource
import uk.co.xlntech.architectureapp.data.api.WorldLobbyApi
import uk.co.xlntech.architectureapp.data.database.WorldLobbyDatabase
import uk.co.xlntech.architectureapp.data.entities.TipSummary
import uk.co.xlntech.architectureapp.utils.SingleLiveEvent
import uk.co.xlntech.architectureapp.utils.component1
import uk.co.xlntech.architectureapp.utils.component2
import uk.co.xlntech.architectureapp.utils.enqueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository(
        private val api: WorldLobbyApi,
        private val db: WorldLobbyDatabase,
        private val locationManager: MyLocationManager,
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
        locationManager.locationLiveData.observeForever(object : Observer<Location> {
            override fun onChanged(location: Location?) {
                Log.d("qwerty", "locationLiveData.onChanged $location")
                loadFeed(0)
                locationManager.locationLiveData.removeObserver(this)
            }
        })
    }

    override fun onItemAtEndLoaded(itemAtEnd: TipSummary) {
        Log.d("qwerty", "onItemAtEndLoaded")
        loadFeed(offset + pageSize)
    }

    fun filter(query: String): LiveData<PagedList<TipSummary>> =
            LivePagedListBuilder(db.tipsDao().getTips("%$query%"), pageSize).build()

    fun search(query: String): LiveData<PagedList<TipSummary>> =
            LivePagedListBuilder(TipsPagedDataSource.getFactory(api, locationManager, query), pageSize).build()

    fun refresh() {
        loadFeed(0)
    }

    private fun loadFeed(offset: Int) {
        Log.d("qwerty", "loadFeed $offset")
        if (isLoading) return // BoundaryCallback can call this multiple times for single list
        isLoading = true
        this.offset = offset
        val (lat, lng) = locationManager.locationLiveData.value
        api.getFeed(skip = offset, limit = pageSize, lat = lat, lng = lng).enqueue(
                onResponse = { feedPage ->
                    Log.d("qwerty", "loadFeed $offset -> ${feedPage.data.map { it.name }}")
                    saveFeed(feedPage.data, offset == 0)
                    isLoading = false
                },
                onFailure = { error ->
                    errors.value = error
                    isLoading = false
                }
        )
    }

    private fun saveFeed(items: List<TipSummary>, clear: Boolean) {
        Log.d("qwerty", "saveFeed ${items.map { it.name }} clear: $clear")
        // observers will be triggered automatically
        dbExecutor.submit {
            db.runInTransaction {
                if (clear) db.tipsDao().clearTips()
                db.tipsDao().insertTips(items)
            }
        }
    }
}