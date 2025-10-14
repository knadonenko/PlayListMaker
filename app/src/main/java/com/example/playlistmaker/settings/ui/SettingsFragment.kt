package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.components.AppBarComponent
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
                Settings()
            }
        }
    }

//    @Preview(device = "id:pixel_8")
    @Composable
    private fun Settings() {
        val darkTheme by viewModel.isDarkTheme.observeAsState(false)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBarComponent(stringResource(R.string.settings)) },
            containerColor = colorResource(R.color.settings_main_color)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SettingsComponent(
                    stringResource(R.string.dark_theme),
                    composableElement = {
                        Switch(
                            checked = darkTheme,
                            onCheckedChange = { viewModel.switchTheme(it) }
                        )
                    })
                SettingsComponent(
                    stringResource(R.string.share_app),
                    composableElement = {
                        IconButton(onClick = {
                            Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_share))
                                type = "text/plain"
                                startActivity(Intent.createChooser(this, null))
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = null,
                                tint = colorResource(R.color.grey)
                            )
                        }
                    })
                SettingsComponent(
                    stringResource(R.string.ask_support),
                    composableElement = {
                        IconButton(onClick = {
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
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.support),
                                contentDescription = null,
                                tint = colorResource(R.color.grey)
                            )
                        }
                    })
                SettingsComponent(
                    stringResource(R.string.user_agreement),
                    composableElement = {
                        IconButton(onClick = {
                            Intent(Intent.ACTION_VIEW).apply {
                                data = getString(R.string.user_agreement_url).toUri()
                                startActivity(Intent.createChooser(this, null))
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_forward),
                                contentDescription = null,
                                tint = colorResource(R.color.grey)
                            )
                        }
                    })
            }
        }
    }

    @Composable
    private fun SettingsComponent(
        text: String,
        composableElement: @Composable () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 16.sp,
                color = colorResource(id = R.color.settings_text_color),
                modifier = Modifier.weight(1f)
            )
            composableElement()
        }
    }
}