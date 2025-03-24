package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.helpers.SharedPrefsConstants.HISTORY_PREFS
import com.example.playlistmaker.helpers.SharedPrefsConstants.PLAYLIST_MAKER_PREFS
import com.example.playlistmaker.player.data.PlayListMediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.network.RetrofitNetworkClient
import com.example.playlistmaker.settings.data.SettingsRepoImpl
import com.example.playlistmaker.settings.data.ThemeStorage
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository

object Creator {

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context),
            TrackStorage(context.getSharedPreferences(HISTORY_PREFS.prefName, Context.MODE_PRIVATE)))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepoImpl (
            ThemeStorage(context.getSharedPreferences(PLAYLIST_MAKER_PREFS.prefName, Context.MODE_PRIVATE))
        )
    }

    fun provideThemeSwitchInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun providePlayerInteractor(trackForPlayer: TrackDto): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(trackForPlayer))
    }

    private fun getPlayerRepository(trackForPlayer: TrackDto): PlayerRepository {
        return PlayerRepositoryImpl(PlayListMediaPlayer(trackForPlayer))
    }
}