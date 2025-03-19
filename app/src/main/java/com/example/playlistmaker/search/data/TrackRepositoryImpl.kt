package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.model.TrackDto
import com.example.playlistmaker.search.network.TrackSearchResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.search.network.NetworkClient

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): List<TrackDto> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TrackSearchResponse).results.map {
                TrackDto(it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl) }
        } else {
            emptyList()
        }
    }
}