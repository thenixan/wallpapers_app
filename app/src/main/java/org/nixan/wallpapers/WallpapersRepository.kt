package org.nixan.wallpapers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException

object WallpapersRepository {

    private val okHttpClient = OkHttpClient.Builder().build()

    val client = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://raw.githubusercontent.com/")
            .build().create(WallpapersApi::class.java)

    fun loadImageIntoBitmap(url: String): Single<Bitmap> {
        return Single.create<Bitmap> { emitter ->
            val request = Request.Builder().url(url).build()
            try {
                val response = okHttpClient.newCall(request).execute()
                val result = BitmapFactory.decodeStream(response.body()?.byteStream()
                        ?: throw IOException("Response body is empty"))
                emitter.onSuccess(result)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}

interface WallpapersApi {

    @GET("/thenixan/wallpapers_backend/master/files.json")
    fun loadWallpapers(): Single<WallpapersList>

}

data class WallpapersList(@SerializedName("wallpapers") val wallpapers: List<Wallpaper>)

data class Wallpaper(@SerializedName("preview_url") val previewUrl: String,
                     @SerializedName("fullsize_url") val fullsizeUrl: String,
                     @SerializedName("author") val author: String)