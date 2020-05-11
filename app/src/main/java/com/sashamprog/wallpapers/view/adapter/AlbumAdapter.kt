package com.sashamprog.wallpapers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import kotlinx.android.synthetic.main.list_item_album.view.*
import kotlinx.android.synthetic.main.list_item_picture.view.image_view_preview

class AlbumAdapter(
    internal var items: MutableList<FavAlbum> = mutableListOf(),
    internal var albumPictures: Map<FavAlbum, List<AlbumPicture>> = mutableMapOf(),
    internal var onClickItemListener: ((View, FavAlbum) -> Unit)? = null,
    internal var onClickRemoveListener: ((View, FavAlbum) -> Unit)? = null
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_album, parent, false)
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val album = items[position]
            val pictures = albumPictures[album]
            view.apply {
                text_view_name.text = album.name

                Glide.with(context)
                    .load(pictures?.firstOrNull()?.url)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(image_view_preview)

                image_view_remove.setOnClickListener {
                    onClickRemoveListener?.invoke(view, album)
                }
            }
            view.setOnClickListener {
                onClickItemListener?.invoke(view, album)
            }
        }
    }
}
