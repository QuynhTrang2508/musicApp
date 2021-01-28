package com.example.musicapp.data.repository

import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.data.source.local.utils.OnDataLoadCallback

class SongRepository private constructor(
    private val local: SongDataSource
) : SongDataSource {

    override fun getSong(callback: OnDataLoadCallback<List<Song>>) {
        local.getSong(callback)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(local: SongDataSource) =
            instance ?: SongRepository(local).also { instance = it }
    }
}
