@file:Suppress("DEPRECATION")

package com.example.musicapp.data.source.local.utils

import android.os.AsyncTask

class LocalAsyncTask<T>(
    private val callback: OnDataLoadCallback<T>,
    private val handle: () -> T
) : AsyncTask<Unit, Unit, T>() {
    override fun doInBackground(vararg params: Unit): T? =
        try {
            handle()
        } catch (e: Exception) {
            null
        }

    override fun onPostExecute(result: T?) {
        super.onPostExecute(result)
        result?.let {
            callback.onSuccess(it)
        } ?: callback.onFail("No songs found on this device")
    }
}
