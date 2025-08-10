package com.example.playlistmaker.search.data

import com.example.playlistmaker.helpers.ApiResponseConstants
import com.example.playlistmaker.helpers.ApiResponseConstants.NO_INTERNET_CONNECTION_CODE
import com.example.playlistmaker.helpers.ApiResponseConstants.NO_INTERNET_CONNECTION_MESSAGE
import com.example.playlistmaker.helpers.ApiResponseConstants.SUCCESS_CODE
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.network.NetworkClient
import com.example.playlistmaker.search.network.TrackSearchResponse
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<TrackDto>>> = flow {

        val response = networkClient.doRequest(
            TrackSearchRequest(
                expression
            )
        )
        when (response.resultCode) {
            NO_INTERNET_CONNECTION_CODE -> {
                emit(
                    Resource.Error(
                        message = NO_INTERNET_CONNECTION_MESSAGE,
                        code = NO_INTERNET_CONNECTION_CODE
                    )
                )
            }

            SUCCESS_CODE -> {
                val listTracks: List<TrackDto> = (response as TrackSearchResponse).results.map {
                    TrackDto(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.artworkUrl60,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
                emit(Resource.Success(listTracks, SUCCESS_CODE))
            }

            else -> {
                emit(Resource.Error(
                    message = ApiResponseConstants.SERVER_ERROR.toString(),
                    code = response.resultCode
                ))
            }
        }
    }

    override fun addTrackToHistory(track: TrackDto) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): ArrayList<TrackDto> {
        return localStorage.getHistory()
    }


}