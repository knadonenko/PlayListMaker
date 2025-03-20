package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.helpers.SharedPrefsConstants
import com.example.playlistmaker.helpers.SharedPrefsConstants.*
import com.example.playlistmaker.settings.data.SettingsRepoImpl
import com.example.playlistmaker.settings.data.ThemeStorage
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository

object Creator {

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
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
}