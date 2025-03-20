package com.example.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App

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
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return modelClass.getConstructor(this::class.java)
                        .newInstance(application)
                }
            }
    }
}