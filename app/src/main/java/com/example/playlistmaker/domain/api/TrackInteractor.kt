package com.example.playlistmaker.domain.api

interface TrackInteractor {
    fun searchSongs(expression: String, consumer: SearchConsumer)
}