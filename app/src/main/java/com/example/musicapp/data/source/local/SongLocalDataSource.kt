package com.example.musicapp.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource

class SongLocalDataSource : SongDataSource.Local {
    @SuppressLint("Recycle")
    override fun getSong(context: Context): MutableList<Song> {
        val songs = mutableListOf<Song>()
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor = context.contentResolver.query(uri, projection, selection, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                songs.add(Song(it))
            }
        }
        return songs
    }

    companion object {
        private var instance: SongLocalDataSource? = null
        fun getInstance() = instance ?: SongLocalDataSource().also { instance = it }
    }
}
