package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.components.SettingsScreenComponent
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreenComponent(
                    viewModel,
                    ::onShareClick, ::onSupportClick, ::onUserAgreementClick
                )
            }
        }
    }

    private fun onShareClick() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_share))
            type = "text/plain"
            startActivity(Intent.createChooser(this, null))
        }
    }

    private fun onSupportClick() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.support_email))
            )
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            startActivity(Intent.createChooser(this, null))
        }
    }

    private fun onUserAgreementClick() {
        Intent(Intent.ACTION_VIEW).apply {
            data = getString(R.string.user_agreement_url).toUri()
            startActivity(Intent.createChooser(this, null))
        }
    }

}