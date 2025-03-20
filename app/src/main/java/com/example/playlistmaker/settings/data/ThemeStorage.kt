package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.helpers.SharedPrefsConstants.DARK_THEME_KEY

class ThemeStorage(private val sp: SharedPreferences) : LocalStorage {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        sp.edit()
            .putBoolean(DARK_THEME_KEY.prefName, darkThemeEnabled)
            .apply()
    }

    override fun getDarkMode(): Boolean {
        return sp.getBoolean(DARK_THEME_KEY.prefName, false)
    }
}