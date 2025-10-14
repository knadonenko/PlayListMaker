package com.example.playlistmaker.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun ButtonView(onClickAction: () -> Unit,
               text: String = stringResource(R.string.clear_history)) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClickAction,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(
                    R.color.settings_text_color
                )
            ),
            shape = RoundedCornerShape(54.dp),
            contentPadding = PaddingValues(
                horizontal = 14.dp,
                vertical = 6.dp
            ),
            modifier = Modifier.height(40.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                letterSpacing = 0.sp
            )
        }
    }

}