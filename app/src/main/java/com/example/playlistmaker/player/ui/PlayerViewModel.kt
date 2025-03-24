package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.helpers.AppConstants.RELOAD_PROGRESS
import com.example.playlistmaker.search.data.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(track: TrackDto) : ViewModel() {
    private val screenState = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = screenState
    private val playerInteractor = Creator.providePlayerInteractor(track)
    private var playerStateEnum: PlayerStateEnum = PlayerStateEnum.STATE_DEFAULT
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

        fun getViewModelFactory(track: TrackDto): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        track
                    ) as T
                }
            }
    }

    init {
        screenState.value = PlayerState.BeginningState(track)
        preparePlayer()
        setOnCompletionListener()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer {
            playerStateEnum = PlayerStateEnum.STATE_PREPARED
            screenState.value = PlayerState.Preparing()
        }
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerStateEnum = PlayerStateEnum.STATE_PREPARED
            handler.removeCallbacks(timer)
            screenState.value = PlayerState.PlayCompleting()
        }
    }

    private fun start() {
        playerInteractor.start()
        playerStateEnum = PlayerStateEnum.STATE_PLAYING
        handler.postDelayed(timer, RELOAD_PROGRESS)
        screenState.value = PlayerState.PlayButtonHandling(playerStateEnum)
    }

    fun pause() {
        playerInteractor.pause()
        playerStateEnum = PlayerStateEnum.STATE_PAUSED
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
            PlayerStateEnum.STATE_PLAYING -> {
                pause()
            }
            PlayerStateEnum.STATE_PREPARED, PlayerStateEnum.STATE_PAUSED -> {
                start()
            }
            PlayerStateEnum.STATE_DEFAULT -> {

            }
        }
    }

}
