package com.sashamprog.wallpapers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import kotlinx.android.synthetic.main.list_item_favorite.view.*
import kotlinx.android.synthetic.main.list_item_picture.view.image_view_favorite
import kotlinx.android.synthetic.main.list_item_picture.view.image_view_preview

class FavoriteAdapter(
    internal var items: MutableList<FavPicture> = mutableListOf(),
    internal var albums: MutableList<FavAlbum> = mutableListOf(),
    internal var onClickItemListener: ((View, FavPicture) -> Unit)? = null,
    internal var onClickFavoriteListener: ((View, FavPicture) -> Unit)? = null,
    internal var onClickAddToAlbumListener: ((View, FavPicture, FavAlbum) -> Unit)? = null
) : RecyclerView.Adapter<FavoriteAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_favorite, parent, false)
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class PictureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val picture = items[position]
            view.apply {
                Glide.with(context)
                    .load(picture.url)
                    .into(image_view_preview)

                image_view_favorite.setImageResource(android.R.drawable.btn_star_big_on)

                spinner_album.adapter =
                    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, albums.map { it.name })

                button_add_to_album.setOnClickListener {
                    onClickAddToAlbumListener?.invoke(
                        view,
                        picture,
                        albums.first { it.name == spinner_album.selectedItem.toString() })
                }

                image_view_favorite.setOnClickListener {
                    onClickFavoriteListener?.invoke(view, picture)
                }

                this.setOnClickListener {
                    onClickItemListener?.invoke(view, picture)
                }
            }
        }
    }
}
