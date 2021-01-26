package com.example.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songs = mutableListOf<Song>()
    var itemClick: ((Int) -> Unit)? = null

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemClick: ((Int) -> Unit)? = null
        fun bindData(song: Song, index: Int) {
            itemView.apply {
                textSongTitle.text = song.title
                textSongArtist.text = song.artist
                Glide.with(context).load(R.drawable.song).circleCrop().into(imageSongItem)
                this.setOnClickListener {
                    itemClick?.invoke(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view).apply {
            itemClick = { index ->
                this@SongAdapter.itemClick?.invoke(index)
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindData(songs[position], position)
    }

    fun updateData(newSong: List<Song>) {
        this.songs = newSong as MutableList<Song>
        notifyDataSetChanged()
    }
}
