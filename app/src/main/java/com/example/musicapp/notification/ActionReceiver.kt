package com.example.musicapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicapp.R

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        p0.sendBroadcast(
            Intent(p0.getString(R.string.action_intent)).putExtra(
                p0.getString(R.string.action_name),
                p1.action
            )
        )
    }
}
