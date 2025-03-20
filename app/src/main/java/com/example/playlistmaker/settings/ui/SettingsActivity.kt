package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.helpers.SharedPrefsConstants.DARK_THEME_KEY
import com.example.playlistmaker.helpers.SharedPrefsConstants.PLAYLIST_MAKER_PREFS
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(application as App)
        )[SettingsViewModel::class.java]


        binding.settingsToolbar.setNavigationOnClickListener { finish() }

        binding.themeSwitcher.apply {
            isChecked = viewModel.isDarkThemeOn()
            setOnCheckedChangeListener { _, checked ->
                viewModel.switchTheme(checked)
            }
        }

        binding.buttonSharing.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_share))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        binding.buttonSupport.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }

        binding.buttonUserAgreement.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                intent.data = Uri.parse(getString(R.string.user_agreement_url))
                startActivity(Intent.createChooser(intent, null))
            }
        }

    }
}