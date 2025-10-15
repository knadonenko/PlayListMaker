package com.example.playlistmaker.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.TimeHelper.longToMillis
import com.example.playlistmaker.search.data.TrackDto

@Composable
fun TrackItemComponent(
    track: TrackDto,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = track.artworkUrl100 ?: R.drawable.track_stub,
            modifier = Modifier.size(48.dp),
            placeholder = painterResource(R.drawable.track_stub),
            error = painterResource(R.drawable.track_stub),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier) {
                Text(
                    text = track.trackName,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = colorResource(id = R.color.search_result_track_text_color)
                )
            }
            Column {
                Text(
                    text = "${track.artistName} • ${track.trackTimeMillis.longToMillis()}",
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = colorResource(id = R.color.search_result_artist_text_color)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.arrow_forward),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}