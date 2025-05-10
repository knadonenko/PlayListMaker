package com.example.playlistmaker.search.network

import com.example.playlistmaker.search.data.Response
import com.example.playlistmaker.search.data.TrackDto

class TrackSearchResponse( val results: ArrayList<TrackDto>) : Response()