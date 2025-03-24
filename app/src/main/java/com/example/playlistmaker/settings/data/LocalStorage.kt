package com.example.playlistmaker.settings.data

interface LocalStorage {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getDarkMode(): Boolean
}