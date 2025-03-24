package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.TrackDto

interface SearchConsumer {
    fun consume(foundTracks: List<TrackDto>?, errorMessage: String?, code: Int)
}