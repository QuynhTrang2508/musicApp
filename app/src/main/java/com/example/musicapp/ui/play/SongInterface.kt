package com.example.musicapp.ui.play

import android.content.Context
import com.example.musicapp.data.model.Song

interface SongInterface {
    interface View {
        fun setPauseButton()
        fun setPlayButton()
        fun resetSeekBar()
        fun startAnimation()
        fun clearAnimation()
        fun initPlay(index: Int)
    }

    interface Presenter {
        fun getSongFromLocal(context: Context): List<Song>
        fun playAction(status: Boolean)
    }
}
