package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.helpers.IntentConstants
import com.example.playlistmaker.helpers.TimeHelper.convertDate
import com.example.playlistmaker.helpers.TimeHelper.convertTimeFromMillis
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumIcon: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<Toolbar>(R.id.player_toolbar).setNavigationOnClickListener {
            finish()
        }

        val track = Gson().fromJson(intent.getStringExtra(IntentConstants.TRACK), Track::class.java)

        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.track_time)
        albumIcon = findViewById(R.id.track_icon)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name)
        country = findViewById(R.id.country_data)

        Glide
            .with(albumIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_stub_big)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.margin_8
                    )
                )
            )
            .into(albumIcon)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        trackTime.text = convertTimeFromMillis(track.trackTimeMillis)

        val date = convertDate(track.releaseDate)
        if (date != null) {
            val formatDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formatDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            collectionNameTitle.visibility = View.GONE
        }
    }

}