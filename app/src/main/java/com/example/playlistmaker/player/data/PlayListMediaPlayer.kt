package com.example.playlistmaker.player.data

import android.media.MediaPlayer

class PlayListMediaPlayer(private val mediaPlayer: MediaPlayer) : PlayerClient {

    override fun preparePlayer(prepare: () -> Unit, dataSource: String) {
        mediaPlayer.setDataSource(dataSource)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { prepare() }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        mediaPlayer.reset()
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}