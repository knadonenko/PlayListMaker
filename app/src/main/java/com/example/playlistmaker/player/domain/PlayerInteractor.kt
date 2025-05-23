package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(prepare: () -> Unit, dataSource: String)
}