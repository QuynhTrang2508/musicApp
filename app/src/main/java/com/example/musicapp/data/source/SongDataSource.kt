package com.example.musicapp.data.source

import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.local.utils.OnDataLoadCallback

interface SongDataSource {
    fun getSong(callback: OnDataLoadCallback<List<Song>>)
}
