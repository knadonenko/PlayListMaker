package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto

interface TrackInteractor {
    fun searchMovies(expression: String, consumer: SearchConsumer)

    interface SearchConsumer {
        fun consume(foundMovies: List<TrackDto>)
    }
}