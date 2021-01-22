package com.example.musicapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.notification.cancelNotification
import com.example.musicapp.notification.sendNotification

@Suppress("DEPRECATION")
class SongService : Service(), PlayMusicInterface {
    var mediaPlayer: MediaPlayer? = null
    private var index = 0
    private var songs = mutableListOf<Song>()

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        initMusicPlayer()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        val song = bundle?.getSerializable(getString(R.string.action_intent))
        createNotificationChannel(
            getString(R.string.info_channelId),
            getString(R.string.info_channelName)
        )
        startForeground(
            1,
            sendNotification(
                this,
                song as Song,
                R.drawable.ic_pause
            )
        )
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder = SongBinder(this)
    override fun onUnbind(intent: Intent?): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        this.stopForeground(true)
        cancelNotification()
        mediaPlayer?.stop()
        stopSelf()
    }

    override fun create(position: Int) {
        val currentSong = songs[position].id.toLong()
        val uri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong)
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        mediaPlayer?.start()
        index = position
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun stop() {
        mediaPlayer?.stop()
    }

    override fun release() {
        mediaPlayer?.release()
    }

    override fun getDuration(): Int? {
        return mediaPlayer?.duration
    }

    override fun getCurrentPosition(): Int? {
        return mediaPlayer?.currentPosition
    }

    override fun isPlaying() = mediaPlayer!!.isPlaying

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun updateNotificationPlay() {
        if (isPlaying()) startForeground(
            1,
            sendNotification(
                this,
                songs[index],
                R.drawable.ic_pause
            )
        )
        else startForeground(
            1,
            sendNotification(
                this,
                songs[index],
                R.drawable.ic_play
            )
        )
    }

    fun updateNotificationChangeSong() {
        startForeground(
            1,
            sendNotification(
                this,
                songs[index],
                R.drawable.ic_pause
            )
        )
    }

    private fun initMusicPlayer() {
        mediaPlayer?.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.isLooping = true
    }

    fun setList(songList: ArrayList<Song>) {
        songs = songList
    }

    fun getIndex(): Int {
        return index
    }

    fun playClick() {
        if (isPlaying()) pause()
        else start()
    }

    fun nextClick() {
        if (songs.isNotEmpty()) {
            if (isPlaying()) {
                stop()
                release()
            }
            if (index < songs.size) {
                index++
                if (index > songs.size - 1) index = 0
                create(index)
            }
        }
    }

    fun prevClick() {
        if (songs.isNotEmpty()) {
            if (isPlaying()) {
                stop()
                release()
            }
            if (index < songs.size) {
                index--
                if (index < 0) index = songs.size - 1
                create(index)
            }
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.msg_remind)
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun cancelNotification() {
        val notificationManager = ContextCompat.getSystemService(
            this
            , NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotification()
    }

    class SongBinder(private val service: SongService) : Binder() {
        fun getService() = service
    }
}
