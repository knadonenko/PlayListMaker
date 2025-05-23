package com.example.playlistmaker.player.data

interface PlayerClient {
    fun preparePlayer(prepare: () -> Unit, dataSource: String)
    fun setOnCompletionListener(onComplete: () -> Unit)
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Int
}