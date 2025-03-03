package com.example.playlistmaker.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.SharedPrefsNames.*
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener {
            finish()
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = getSharedPreferences(PLAYLIST_MAKER_PREFS.prefName, MODE_PRIVATE)
            .getBoolean(DARK_THEME_KEY.prefName, false)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            (applicationContext as App).saveTheme(checked)
        }
        findViewById<Button>(R.id.button_sharing).setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_share))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }
        findViewById<Button>(R.id.button_support).setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }
        findViewById<Button>(R.id.button_user_agreement).setOnClickListener() {
            Intent().apply {
                action = Intent.ACTION_VIEW
                intent.data = Uri.parse(getString(R.string.user_agreement_url))
                startActivity(Intent.createChooser(intent, null))
            }
        }
    }
}