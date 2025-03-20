package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun switchTheme(isDarkMode: Boolean)
    fun isDarkModeOn(): Boolean
    fun setCurrentTheme()
}