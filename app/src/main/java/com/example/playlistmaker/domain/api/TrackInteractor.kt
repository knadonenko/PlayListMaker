package com.example.playlistmaker.domain.api

interface TrackInteractor {
    fun searchMovies(expression: String, consumer: SearchConsumer)
}