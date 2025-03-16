package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto

interface SearchConsumer {
    fun consume(foundMovies: List<TrackDto>)
}