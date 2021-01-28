package com.example.musicapp.ui.play

import com.example.musicapp.data.model.Song

interface SongInterface {
    interface View {
        fun setPauseButton()
        fun setPlayButton()
        fun resetSeekBar()
        fun startAnimation()
        fun clearAnimation()
        fun initPlay(index: Int)
        fun updateAdapter(listSong: List<Song>)
        fun showError(error: String)
    }

    interface Presenter {
        fun getSongFromLocal()
        fun playAction(status: Boolean)
    }
}
