package com.example.playlistmaker.uicomponents

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
    Column (Modifier
        .fillMaxWidth()
        .padding(4.dp)){
        // Карточка с обложкой плейлиста
        val filePath = File(
            LocalContext.current.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES
        )
        Card(
            modifier = Modifier.aspectRatio(1f)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(0.dp),
            onClick = onClick
        ) {
            val image = playlist.cover?.let { File(filePath, playlist.cover) }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = image)
                    .build(),
                modifier = Modifier.fillMaxSize().background(colorResource(R.color.settings_main_color)),
                placeholder = painterResource(R.drawable.track_stub),
                error = painterResource(R.drawable.track_stub),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        // Название плейлиста
        Text(
            text = playlist.name,
            modifier = Modifier.padding(top = 2.dp),
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
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = colorResource(R.color.search_result_track_text_color),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}