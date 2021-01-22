package com.example.musicapp.data.repository

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource

class SongRepository private constructor(
    private val local: SongDataSource.Local
) : SongDataSource.Local {
    override fun getSong(context: Context): MutableList<Song> {
        return local.getSong(context)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(local: SongDataSource.Local) =
            instance ?: SongRepository(local).also { instance = it }
    }
}
