package com.sashamprog.wallpapers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.R
import kotlinx.android.synthetic.main.list_item_picture.view.*

class PictureAdapter(
        internal var items: List<Picture> = listOf(),
        internal var onClickItemListener: ((View, Picture) -> Unit)? = null,
        internal var onClickFavoriteListener: ((View, Picture) -> Unit)? = null
) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_picture, parent, false)
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
                        .load(picture.preview)
                        .into(image_view_preview)

                image_view_favorite.setImageResource(
                        if (picture.isFavorite)
                            android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
                )

                image_view_favorite.setOnClickListener {
                    onClickFavoriteListener?.invoke(view, picture)
                }
            }
            view.setOnClickListener {
                onClickItemListener?.invoke(view, picture)
            }
        }
    }
}
