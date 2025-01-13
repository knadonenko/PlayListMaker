package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.helpers.SharedPrefsNames.DARK_THEME_KEY
import com.example.playlistmaker.helpers.SharedPrefsNames.PLAYLIST_MAKER_PREFS

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFS.prefName, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY.prefName, isDarkTheme(this))
        switchTheme(darkTheme)
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

    private fun isDarkTheme(context: Context): Boolean {
        val isDarkMode: Boolean
        val currentNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDarkMode = when ((currentNightMode)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        return isDarkMode
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        getSharedPreferences(PLAYLIST_MAKER_PREFS.prefName, MODE_PRIVATE)
            .edit()
            .putBoolean(DARK_THEME_KEY.prefName, darkThemeEnabled)
            .apply()
    }

}