package com.example.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(var clickItem: ((Int) -> Unit)) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songs = mutableListOf<Song>()

    class SongViewHolder(itemView: View, private var itemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemClick.invoke(adapterPosition)
            }
        }

        fun bindData(song: Song) {
            itemView.apply {
                textSongTitle.text = song.title
                textSongArtist.text = song.artist
                Glide.with(context).load(R.drawable.song).circleCrop().into(imageSongItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view, clickItem)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindData(songs[position])
    }

    fun updateData(newSong: List<Song>) {
        this.songs = newSong.toMutableList()
        notifyDataSetChanged()
    }
}