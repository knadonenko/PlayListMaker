package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto

interface TrackRepository {
    fun searchTracks(expression: String): List<TrackDto>
}