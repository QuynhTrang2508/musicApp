package com.example.musicapp.data.source

import android.content.Context
import com.example.musicapp.data.model.Song

interface SongDataSource {
    interface Local {
        fun getSong(context: Context): MutableList<Song>
    }
}
