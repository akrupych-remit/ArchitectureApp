package uk.co.xlntech.architectureapp.data.api

import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.xlntech.architectureapp.data.entities.FeedPage
import uk.co.xlntech.architectureapp.data.entities.TipSummary

class TipsPagedDataSource(
        private val api: WorldLobbyApi,
        private val query: String
) : PositionalDataSource<TipSummary>() {

    companion object {
        fun getFactory(api: WorldLobbyApi, query: String) = object : DataSource.Factory<Int, TipSummary>() {
            override fun create() = TipsPagedDataSource(api, query)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<TipSummary>) {
        api.getFeed(skip = 0, limit = params.requestedLoadSize, search = query).enqueue(object : Callback<FeedPage> {
            override fun onResponse(call: Call<FeedPage>, response: Response<FeedPage>) {
                if (response.isSuccessful) callback.onResult(response.body()!!.data, 0, response.body()!!.totalCount)
                else callback.onResult(emptyList(), 0)
            }
            override fun onFailure(call: Call<FeedPage>?, t: Throwable?) {
                callback.onResult(emptyList(), 0)
            }
        })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<TipSummary>) {
        api.getFeed(skip = params.startPosition, limit = params.loadSize, search = query).enqueue(object : Callback<FeedPage> {
            override fun onResponse(call: Call<FeedPage>, response: Response<FeedPage>) {
                if (response.isSuccessful) callback.onResult(response.body()!!.data)
                else callback.onResult(emptyList())
            }
            override fun onFailure(call: Call<FeedPage>?, t: Throwable?) {
                callback.onResult(emptyList())
            }
        })
    }
}