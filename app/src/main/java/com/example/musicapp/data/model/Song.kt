package com.example.musicapp.data.model

import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize

@Parcelize
class Song(var id: Int, var title: String, var artist: String) : Parcelable {

    constructor(cursor: Cursor) : this(
        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST))
    )
}
