package com.example.musicapp.data.repository

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.data.source.local.utils.OnDataLoadCallback

class SongRepository private constructor(
    private val local: SongDataSource.Local
) : SongDataSource.Local {

    override fun getSong(context: Context, callback: OnDataLoadCallback<List<Song>>) {
        local.getSong(context, callback)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(local: SongDataSource.Local) =
            instance ?: SongRepository(local).also { instance = it }
    }
}
