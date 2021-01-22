package com.example.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(
    private var songs: MutableList<Song>,
    private var actionClick: ClickItem
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(song: Song) {
            itemView.apply {
                textSongTitle.text = song.title
                textSongArtist.text = song.artist
                Glide.with(context).load(R.drawable.song).circleCrop().into(imageSongItem)
            }
        }

        fun clickSong(song: Song, action: ClickItem) {
            itemView.setOnClickListener {
                action.clickSong(song, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindData(songs[position])
        holder.clickSong(songs[position], actionClick)
    }

    interface ClickItem {
        fun clickSong(song: Song, position: Int)
    }
}
