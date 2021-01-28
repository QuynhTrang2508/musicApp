package com.example.musicapp.data.source.local.utils

interface OnDataLoadCallback<T> {
    fun onSuccess(data: T)
    fun onFail(message: String)
}
