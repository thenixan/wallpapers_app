package org.nixan.wallpapers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class WallpapersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val wallpaperPreview: ImageView = itemView.findViewById(R.id.wallpaper_preview)
    val wallpaperAuthor: TextView = itemView.findViewById(R.id.wallpaper_author)
}

class WallpapersAdapter : RecyclerView.Adapter<WallpapersViewHolder>() {

    var items: List<Wallpaper> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var callback: OnWallpaperSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_wallpaper, parent, false)
        return WallpapersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener { callback?.onWallpaperSelected(item.fullsizeUrl) }
        holder.wallpaperAuthor.text = item.author
        Picasso.get().load(item.previewUrl).into(holder.wallpaperPreview)
    }

}

interface OnWallpaperSelectedListener {
    fun onWallpaperSelected(wallpaperUrl: String)
}