package com.example.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.player.ui.PlayerViewModel

class SettingsViewModel(application: App) : AndroidViewModel(application) {

    private val switchThemeInteractor = application.themeSwitcherInteractor

    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switchTheme(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return switchThemeInteractor.isDarkModeOn()
    }

    companion object {
        fun getViewModelFactory(application: App): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        application = application
                    ) as T
                }
            }
    }
}