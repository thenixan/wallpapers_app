package org.nixan.wallpapers

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object WallpapersRepository {

    val client = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://raw.githubusercontent.com/")
            .build().create(WallpapersApi::class.java)

}

interface WallpapersApi {

    @GET("/thenixan/wallpapers_backend/master/files.json")
    fun loadWallpapers(): Single<WallpapersList>

}

data class WallpapersList(@SerializedName("wallpapers") val wallpapers: List<Wallpaper>)

data class Wallpaper(@SerializedName("preview_url") val previewUrl: String,
                     @SerializedName("fullsize_url") val fullsizeUrl: String,
                     @SerializedName("author") val author: String)