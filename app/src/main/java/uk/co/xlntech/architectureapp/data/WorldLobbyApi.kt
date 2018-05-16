package uk.co.xlntech.architectureapp.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.xlntech.architectureapp.data.entities.FeedPage

interface WorldLobbyApi {

    companion object {

        private const val BASE_URL = "https://us-central1-worldlobby-2ff52.cloudfunctions.net/api/v2/"

        fun create(): WorldLobbyApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WorldLobbyApi::class.java)
    }

    @GET("tips")
    fun getFeed(
            @Query("skip") skip: Int? = null,
            @Query("limit") limit: Int? = null,
            @Query("lat") lat: Double? = null,
            @Query("lng") lng: Double? = null,
            @Query("radius") radius: Int? = null,
            @Query("search") search: String? = null,
            @Query("category[]") categories: List<String>? = null
    ): Call<FeedPage>
}