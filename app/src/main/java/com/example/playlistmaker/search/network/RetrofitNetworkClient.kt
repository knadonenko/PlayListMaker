package com.example.playlistmaker.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.Response
import com.example.playlistmaker.search.data.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val baseURL = "https://itunes.apple.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(SearchAPI::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.searchTrack(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                e.printStackTrace()
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}