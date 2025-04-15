package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.helpers.AppConstants.RELOAD_PROGRESS
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerViewModel.PlayerStateEnum.*
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.TrackInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val screenState = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = screenState
    private var playerStateEnum: PlayerStateEnum = STATE_DEFAULT
    private val handler: Handler = Handler(Looper.getMainLooper())

    enum class PlayerStateEnum {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    private val timer =
        object : Runnable {
            override fun run() {
                updateTimer(getCurrentPosition())
                handler.postDelayed(
                    this,
                    RELOAD_PROGRESS,
                )
            }
        }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    init {
        setOnCompletionListener()
    }

    private fun preparePlayer(trackDto: TrackDto) {
        playerInteractor.preparePlayer({
            playerStateEnum = STATE_PREPARED
            screenState.value = PlayerState.Preparing()
        }, trackDto.previewUrl)
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerStateEnum = STATE_PREPARED
            handler.removeCallbacks(timer)
            screenState.value = PlayerState.PlayCompleting()
        }
    }

    private fun start() {
        playerInteractor.start()
        playerStateEnum = STATE_PLAYING
        handler.postDelayed(timer, RELOAD_PROGRESS)
        screenState.value = PlayerState.PlayButtonHandling(playerStateEnum)
    }

    fun pause() {
        playerInteractor.pause()
        playerStateEnum = STATE_PAUSED
        handler.removeCallbacks(timer)
        screenState.value = PlayerState.PlayButtonHandling(playerStateEnum)
    }

    private fun updateTimer(time: String) {
        screenState.postValue(PlayerState.TimerUpdating(time))
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(playerInteractor.getCurrentTime()).toString()
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.onDestroy()
    }

    fun playbackControl() {
        when (playerStateEnum) {
            STATE_PLAYING -> {
                pause()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                start()
            }

            STATE_DEFAULT -> {

            }
        }
    }

    fun setInitialTrack(trackDto: TrackDto) {
        screenState.value = PlayerState.BeginningState(trackDto)
        preparePlayer(trackDto)
    }

    fun getTrack(): TrackDto {
        return trackInteractor
            .getHistory()
            .first()
    }

}
