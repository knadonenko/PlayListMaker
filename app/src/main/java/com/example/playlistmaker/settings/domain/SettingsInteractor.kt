package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun switchTheme(isDarkMode: Boolean)
    fun isDarkModeOn(): Boolean
    fun setCurrentTheme()
}