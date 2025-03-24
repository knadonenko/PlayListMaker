package com.example.playlistmaker.search.network

import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.data.Response

class TrackSearchResponse (val resultCount: Int, val results: ArrayList<TrackDto>) : Response()