package com.example.playlistmaker.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeHelper {

    fun convertDate(date: String) : Date? {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
    }

    fun Long.longToMillis(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
    }

}