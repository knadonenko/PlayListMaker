package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {


    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.switchTheme(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return settingsInteractor.isDarkModeOn()
    }

}