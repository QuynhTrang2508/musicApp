package com.example.musicapp.ui.play

import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.data.source.local.utils.OnDataLoadCallback

class SongPresenter(
    private val songView: SongInterface.View,
    private val songRepository: SongRepository
) : SongInterface.Presenter {

    override fun getSongFromLocal() {
        songRepository.getSong(object : OnDataLoadCallback<List<Song>> {
            override fun onSuccess(data: List<Song>) {
                songView.updateAdapter(data)
            }

            override fun onFail(message: String) {
                songView.showError(message)
            }
        })
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
