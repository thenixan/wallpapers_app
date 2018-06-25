package org.nixan.wallpapers

import android.app.WallpaperManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), OnWallpaperSelectedListener {

    override fun onWallpaperSelected(wallpaperUrl: String) {
        WallpapersRepository.loadImageIntoBitmap(wallpaperUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    WallpaperManager.getInstance(this)
                            .setBitmap(result)
                    Toast.makeText(this, "Wallpaper was set!", Toast.LENGTH_LONG).show()
                }, { error ->
                    error.printStackTrace()
                })
    }

    private val adapter = WallpapersAdapter()

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.wallpapers_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadItems()
        adapter.callback = this
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItems() {
        WallpapersRepository.client.loadWallpapers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    adapter.items = result.wallpapers
                }, { error ->
                    error.printStackTrace()
                })
    }
}
