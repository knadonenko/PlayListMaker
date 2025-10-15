package com.example.playlistmaker.uicomponents

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.library.data.Playlist
import java.io.File

@Composable
fun PlaylistCardComponent(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Карточка с обложкой плейлиста
        val filePath = File(
            LocalContext.current.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES
        )
        Card(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            elevation = CardDefaults.cardElevation(0.dp),
            onClick = onClick
        ) {
            val image = playlist.cover?.let { File(filePath, playlist.cover) }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = image)
                    .build(),
                modifier = Modifier.fillMaxSize().background(colorResource(R.color.white)),
                placeholder = painterResource(R.drawable.track_stub),
                error = painterResource(R.drawable.track_stub),
                contentDescription = null,
            )
        }

        // Название плейлиста
        Text(
            text = playlist.name,
            modifier = Modifier
                .padding(top = 8.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = colorResource(R.color.search_result_track_text_color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Количество треков
        Text(
            text = playlist.tracksCount.toString(),
            modifier = Modifier
                .padding(top = 8.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = colorResource(R.color.search_result_track_text_color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}