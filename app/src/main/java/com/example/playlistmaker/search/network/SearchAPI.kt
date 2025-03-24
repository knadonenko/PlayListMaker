package com.example.playlistmaker.search.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {
    @GET("search?entity=song")
    fun searchTrack (@Query("term") text: String) : Call<TrackSearchResponse>
}