package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.helpers.SharedPrefsConstants.DARK_THEME_KEY
import com.example.playlistmaker.helpers.SharedPrefsConstants.PLAYLIST_MAKER_PREFS
import com.example.playlistmaker.settings.domain.SettingsInteractor

class App : Application() {

    lateinit var themeSwitcherInteractor: SettingsInteractor

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.setCurrentTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        getSharedPreferences(PLAYLIST_MAKER_PREFS.prefName, MODE_PRIVATE)
            .edit()
            .putBoolean(DARK_THEME_KEY.prefName, darkThemeEnabled)
            .apply()
    }

}