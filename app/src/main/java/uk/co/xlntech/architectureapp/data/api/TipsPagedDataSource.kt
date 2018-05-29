package uk.co.xlntech.architectureapp.data.api

import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource
import android.util.Log
import uk.co.xlntech.architectureapp.data.MyLocationManager
import uk.co.xlntech.architectureapp.data.entities.TipSummary
import uk.co.xlntech.architectureapp.utils.component1
import uk.co.xlntech.architectureapp.utils.component2
import uk.co.xlntech.architectureapp.utils.enqueue

class TipsPagedDataSource(
        private val api: WorldLobbyApi,
        private val locationManager: MyLocationManager,
        private val query: String
) : PositionalDataSource<TipSummary>() {

    companion object {
        fun getFactory(api: WorldLobbyApi, locationManager: MyLocationManager, query: String) =
                object : DataSource.Factory<Int, TipSummary>() {
                    override fun create() = TipsPagedDataSource(api, locationManager, query)
                }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<TipSummary>) {
        Log.d("qwerty", "loadInitial")
        val (lat, lng) = locationManager.locationLiveData.value
        api.getFeed(skip = 0, limit = params.requestedLoadSize, search = query, lat = lat, lng = lng).enqueue(
                onResponse = { feedPage ->
                    Log.d("qwerty", "loadInitial -> ${feedPage.data.map { it.name }}")
                    callback.onResult(feedPage.data, 0, feedPage.totalCount)
                },
                onFailure = { callback.onResult(emptyList(), 0) }
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<TipSummary>) {
        Log.d("qwerty", "loadRange ${params.startPosition} ${params.loadSize}")
        val (lat, lng) = locationManager.locationLiveData.value
        api.getFeed(skip = params.startPosition, limit = params.loadSize, search = query, lat = lat, lng = lng).enqueue(
                onResponse = { feedPage ->
                    Log.d("qwerty", "loadRange -> ${feedPage.data.map { it.name }}")
                    callback.onResult(feedPage.data)
                },
                onFailure = { callback.onResult(emptyList()) }
        )
    }
}