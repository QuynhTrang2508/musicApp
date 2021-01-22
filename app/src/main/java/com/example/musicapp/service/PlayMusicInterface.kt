package com.example.musicapp.service

interface PlayMusicInterface {
    fun create(position: Int)
    fun start()
    fun pause()
    fun stop()
    fun release()
    fun getDuration(): Int?
    fun getCurrentPosition(): Int?
    fun isPlaying(): Boolean
    fun seekTo(position: Int)
}
