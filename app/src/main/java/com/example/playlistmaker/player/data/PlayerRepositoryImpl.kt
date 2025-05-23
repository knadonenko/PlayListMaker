package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient) : PlayerRepository {
    override fun preparePlayer(prepare: () -> Unit, dataSource: String) {
        playerClient.preparePlayer(prepare, dataSource)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
    }

    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Int {
        return playerClient.getCurrentTime()
    }
}