package com.example.playlistmaker.player.domain

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun start() {
        playerRepository.start()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(onComplete)
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(prepare: () -> Unit, dataSource: String) {
        playerRepository.preparePlayer(prepare, dataSource)
    }
}