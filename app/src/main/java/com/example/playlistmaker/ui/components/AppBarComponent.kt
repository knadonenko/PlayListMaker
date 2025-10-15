package com.example.playlistmaker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarComponent(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 22.sp,
                color = colorResource(R.color.settings_text_color)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.settings_main_color))
    )
}