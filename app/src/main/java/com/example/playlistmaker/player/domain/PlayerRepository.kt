package com.example.playlistmaker.player.domain

interface PlayerRepository {
    fun preparePlayer(prepare: () -> Unit)
    fun setOnCompletionListener(onComplete: () -> Unit)
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Int
}