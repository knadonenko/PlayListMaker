package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.network.TrackSearchResponse
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.helpers.ApiResponseConstants
import com.example.playlistmaker.search.network.NetworkClient
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient,
    private val localStorage: LocalStorage) : TrackRepository {

    override fun searchTracks(expression: String): Resource<List<TrackDto>> {

        val response = networkClient.doRequest(
            TrackSearchRequest(
                expression
            )
        )
        return when (response.resultCode) {
            ApiResponseConstants.NO_INTERNET_CONNECTION_CODE -> {
                Resource.Error(
                    message = ApiResponseConstants.NO_INTERNET_CONNECTION_MESSAGE
                )
            }
            ApiResponseConstants.SUCCESS_CODE -> {
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
                Resource.Error(message = ApiResponseConstants.SERVER_ERROR.toString())
            }
        }
    }

    override fun addTrackToHistory(track: TrackDto) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): List<TrackDto> {
        return localStorage.getHistory()
    }


}