package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.search.network.RetrofitNetworkClient
import com.example.playlistmaker.search.model.TrackDto
import com.example.playlistmaker.search.network.TrackSearchResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.util.Resource

class TrackInteractorImpl(private val networkClient: RetrofitNetworkClient) : TrackRepository {

    override fun searchTracks(expression: String) : Resource<List<TrackDto>> {
        val response = networkClient.doRequest(expression)
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    TrackDto(it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl)
                })
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}