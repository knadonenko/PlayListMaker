package com.example.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepoImpl(private val themeStorage: ThemeStorage) : SettingsRepository {
    override fun switchTheme(isDarkMode: Boolean) {
        themeStorage.switchTheme(isDarkMode)
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.getDarkMode()
    }

    override fun setCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}