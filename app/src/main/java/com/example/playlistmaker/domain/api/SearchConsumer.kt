package com.example.playlistmaker.domain.api

import com.example.playlistmaker.search.model.TrackDto

interface SearchConsumer {
    fun consume(foundTracks: List<TrackDto>?, errorMessage: String?)
}