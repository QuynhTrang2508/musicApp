package com.example.musicapp.data.source

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.local.utils.OnDataLoadCallback

interface SongDataSource {
    interface Local {
        fun getSong(context: Context, callback: OnDataLoadCallback<List<Song>>)
    }
}
