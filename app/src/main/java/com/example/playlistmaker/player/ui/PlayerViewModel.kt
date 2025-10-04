package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.LibraryInteractor
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.TrackInteractor
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackInteractor: TrackInteractor,
    private val libraryInteractor: LibraryInteractor,
) : ViewModel() {

    private val screenState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = screenState
    private var isFavorite: Boolean = false
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavoriteLiveData
    private var audioPlayerControl: AudioPlayerControl? = null

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun playbackControl() {
        if (screenState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun setPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl
        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                screenState.postValue(it)
            }
        }
        isTrackFavorite(getTrack())
    }

    fun getTrack(): TrackDto {
        return trackInteractor.getCurrentTrack()
    }

    fun addTrackToLiked() {
        isFavorite = !isFavorite
        isFavoriteLiveData.value = isFavorite
        viewModelScope.launch {
            if (isFavorite) {
                libraryInteractor.saveTrack(getTrack())
            } else {
                libraryInteractor.deleteTrack(getTrack().trackId)
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

    fun showNotification() {
        audioPlayerControl?.showNotification()
    }

    fun hideNotification() {
        audioPlayerControl?.hideNotification()
    }

}
