package com.example.musicapp.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.musicapp.R
import com.example.musicapp.data.model.Song

private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun sendNotification(context: Context, song: Song, icon: Int): Notification {
    val mediaSessionCompat = MediaSessionCompat(context, context.getString(R.string.app_name))

    val playIntent = Intent(context, ActionReceiver::class.java)
    playIntent.action = context.getString(R.string.action_play)
    val playPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, playIntent, FLAGS)

    val prevIntent = Intent(context, ActionReceiver::class.java)
    prevIntent.action = context.getString(R.string.action_prev)
    val prevPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, prevIntent, FLAGS)

    val nextIntent = Intent(context, ActionReceiver::class.java)
    nextIntent.action = context.getString(R.string.action_next)
    val nextPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, nextIntent, FLAGS)

    return NotificationCompat.Builder(context, context.getString(R.string.info_channel_id))
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(song.title)
        .setContentText(song.artist)
        .setOnlyAlertOnce(true)
        .addAction(R.drawable.ic_prev, context.getString(R.string.action_next), prevPendingIntent)
        .addAction(icon, context.getString(R.string.action_play), playPendingIntent)
        .addAction(R.drawable.ic_next, context.getString(R.string.action_next), nextPendingIntent)
        .setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(mediaSessionCompat.sessionToken)
        )
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}

fun NotificationManager.cancelNotification() {
    cancelAll()
}
