package com.example.musicapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicapp.R

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        fun getIntent(): String? = intent.action
        context.sendBroadcast(
            Intent(context.getString(R.string.action_intent)).putExtra(
                context.getString(R.string.action_data),
                getIntent()
            )
        )
    }
}
