package com.example.musicapp.ui.play

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.data.source.local.SongLocalDataSource
import com.example.musicapp.service.SongService
import com.example.musicapp.ui.adapter.SongAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SongInterface.View,
    SeekBar.OnSeekBarChangeListener {

    private var songPresenter: SongInterface.Presenter? = null
    private var songAdapter: SongAdapter = SongAdapter(clickItem = { index -> clickSong(index) })
    private var songService: SongService? = null
    private var animation: Animation? = null
    private var musicBound = false
    private var playIntent: Intent? = null
    private var songs = mutableListOf<Song>()
    private val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(notificationReceiver, IntentFilter(getString(R.string.action_intent)))
        songPresenter =
            SongPresenter(this, SongRepository.getInstance(SongLocalDataSource.getInstance(this)))
        if (checkPermission()) init()
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(permission, 0)
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        buttonNext.setOnClickListener { nextAction() }
        buttonPlay.setOnClickListener { playAction() }
        buttonPrev.setOnClickListener { prevAction() }
        seekBarSong.setOnSeekBarChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        songService?.stop()
        unbindService(musicConnection)
        stopService(getServiceIntent())
        unregisterReceiver(notificationReceiver)
    }

    override fun setPauseButton() = buttonPlay.setImageResource(R.drawable.ic_pause)

    override fun setPlayButton() = buttonPlay.setImageResource(R.drawable.ic_play)

    override fun resetSeekBar() {
        seekBarSong.progress = 0
        textDuration.text = getString(R.string.title_seek)
    }

    override fun startAnimation() = imageSong.startAnimation(animation)

    override fun clearAnimation() = imageSong.clearAnimation()

    override fun initPlay(index: Int) {
        group.visibility = View.VISIBLE
        setPauseButton()
        Glide.with(this).load(R.drawable.music).circleCrop().into(imageSong)
        textSongName.text = songs[index].title
        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        textMax.text = simpleDateFormat.format(songService?.getDuration())
        seekBarSong.max = songService?.getDuration()!!
        updateTime()
        startAnimation()
        startService(index, getString(R.string.action_create_notification))
    }

    override fun updateAdapter(listSong: List<Song>) {
        songs = listSong as MutableList<Song>
        songAdapter.updateData(listSong)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        textDuration.text = simpleDateFormat.format(p0.progress)
        songService!!.seekTo(p0.progress)
        if (textDuration.text == textMax.text) {
            nextAction()
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar) {

    }

    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as SongService.SongBinder
            songService = binder.getService()
            songService?.setList(songs as ArrayList<Song>)
            musicBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            musicBound = false
        }
    }

    private fun init() {
        checkPermission()
        recyclerListSong.adapter = songAdapter
        songPresenter!!.getSongFromLocal()
        if (playIntent == null) {
            playIntent = Intent(this, SongService::class.java)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (p in permission) {
                val status = checkSelfPermission(p)
                if (status == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    private fun nextAction() {
        songService?.nextClick()
        initPlay(songService!!.getIndex())
        resetSeekBar()
    }

    private fun playAction() {
        songService?.playClick()
        updateTime()
        songService?.updateNotificationPlay()
        songPresenter?.playAction(songService!!.isPlaying())
    }

    private fun prevAction() {
        songService?.prevClick()
        initPlay(songService!!.getIndex())
        resetSeekBar()
    }

    private fun updateTime() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (songService?.mediaPlayer != null && songService!!.isPlaying()) {
                        seekBarSong.post {
                            seekBarSong.progress = songService?.getCurrentPosition()!!
                        }
                    } else {
                        timer.cancel()
                        timer.purge()
                    }
                }
            }

        }, 0, 1000)
    }

    private fun clickSong(position: Int) {
        songService.let {
            it?.stop()
            it?.release()
            it?.create(position)
        }
        initPlay(position)
        resetSeekBar()
    }

    private fun startService(index: Int, action: String) {
        val serviceIntent = Intent(this, SongService::class.java)
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.action_intent), songs[index])
        serviceIntent.putExtras(bundle).action = action
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun getServiceIntent() = Intent(this, SongService::class.java)

    private var notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras?.getString(getString(R.string.action_data))) {
                getString(R.string.action_next) -> {
                    nextAction()
                    songService?.updateNotificationChangeSong()
                }
                getString(R.string.action_play) -> {
                    playAction()
                    songService?.updateNotificationPlay()
                }
                getString(R.string.action_prev) -> {
                    prevAction()
                    songService?.updateNotificationChangeSong()
                }
            }
        }
    }
}
