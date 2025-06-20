package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.LibraryInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerViewModel.PlayerStateEnum.STATE_DEFAULT
import com.example.playlistmaker.player.ui.PlayerViewModel.PlayerStateEnum.STATE_PAUSED
import com.example.playlistmaker.player.ui.PlayerViewModel.PlayerStateEnum.STATE_PLAYING
import com.example.playlistmaker.player.ui.PlayerViewModel.PlayerStateEnum.STATE_PREPARED
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.TrackInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackInteractor: TrackInteractor,
    private val libraryInteractor: LibraryInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = screenState
    private var playerStateEnum: PlayerStateEnum = STATE_DEFAULT

    private var timerJob: Job? = null
    private var isFavorite: Boolean = false

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavoriteLiveData

    enum class PlayerStateEnum {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
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
            timerJob?.cancel()
            screenState.value = PlayerState.PlayCompleting()
        }
    }

    private fun start() {
        playerInteractor.start()
        playerStateEnum = STATE_PLAYING
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerStateEnum == STATE_PLAYING) {
                delay(300L)
                screenState.value = PlayerState.PlayButtonHandling(playerStateEnum)
                updateTimer(getCurrentPosition())
            }
        }
    }

    fun pause() {
        playerInteractor.pause()
        playerStateEnum = STATE_PAUSED
        timerJob?.cancel()
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

            STATE_DEFAULT -> {}
        }
    }

    fun setInitialTrack(trackDto: TrackDto) {
        screenState.value = PlayerState.BeginningState(trackDto)
        isTrackFavorite(trackDto)
        preparePlayer(trackDto)
    }

    fun getTrack(): TrackDto {
        return trackInteractor.getCurrentTrack()
    }

    fun addTrackToLiked(track: TrackDto) {
        isFavorite = !isFavorite
        isFavoriteLiveData.value = isFavorite
        viewModelScope.launch {
            if (isFavorite) {
                libraryInteractor.saveTrack(track)
            }
            else {
                libraryInteractor.deleteTrack(track.trackId)
            }
        }
    }

    private fun isTrackFavorite(trackDto: TrackDto) {
        viewModelScope.launch {
            libraryInteractor.isFavorite(trackDto.trackId)
                .collect {
                    isFavorite = it
                    isFavoriteLiveData.postValue(isFavorite)
                }
        }
    }

}
