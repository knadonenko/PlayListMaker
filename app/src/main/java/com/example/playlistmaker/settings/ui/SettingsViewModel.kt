package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val isDarkThemeState = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> get() = isDarkThemeState

    init {
        isDarkThemeState.value = settingsInteractor.isDarkModeOn()
    }

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.switchTheme(isChecked)
        isDarkThemeState.value = settingsInteractor.isDarkModeOn()
    }

}