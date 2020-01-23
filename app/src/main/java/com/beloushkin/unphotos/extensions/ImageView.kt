package com.beloushkin.unphotos.extensions

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beloushkin.unphotos.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream

fun ImageView.loadNetworkImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.placeholder)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

fun ImageView.saveNetworkImageToFileAsync(uri: String?, file: File):Deferred<File?> {
    val requestOptions = RequestOptions()
        .downsample(DownsampleStrategy.CENTER_INSIDE)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)

    return GlobalScope.async (Dispatchers.IO) {

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(uri)
            .apply(requestOptions)
            .submit()
            .get()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()
            Log.i("ImageView", "Image saved.")
            file
        } catch (e: Exception) {
            Log.i("ImageView", "Failed to save image.")
            null
        }
    }

}