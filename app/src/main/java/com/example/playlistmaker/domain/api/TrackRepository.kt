package com.example.playlistmaker.domain.api

import com.example.playlistmaker.search.model.TrackDto
import com.example.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<TrackDto>>
}