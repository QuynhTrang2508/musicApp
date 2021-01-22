package com.example.musicapp.ui.play

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository

class SongPresenter(
    private val songView: SongInterface.View,
    private val songRepository: SongRepository
) : SongInterface.Presenter {
    override fun getSongFromLocal(context: Context): List<Song> {
        return songRepository.getSong(context)
    }

    override fun playAction(status: Boolean) {
        if (status) {
            songView.setPauseButton()
            songView.startAnimation()
        } else {
            songView.setPlayButton()
            songView.clearAnimation()
        }
    }
}
