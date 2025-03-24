package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository):
    SettingsInteractor {

    override fun switchTheme(isDarkMode: Boolean) {
        settingsRepository.switchTheme(isDarkMode)
    }

    override fun isDarkModeOn(): Boolean {
        return settingsRepository.isDarkModeOn()
    }

    override fun setCurrentTheme() {
        settingsRepository.setCurrentTheme()
    }
}