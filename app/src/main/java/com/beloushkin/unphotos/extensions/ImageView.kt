package com.beloushkin.unphotos.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.palette.graphics.Palette
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.beloushkin.unphotos.util.getComplementaryColor
import com.beloushkin.unphotos.util.getContrastVersionForColor


fun ImageView.loadNetworkImage(uri: String?, progressDrawable: CircularProgressDrawable
                               , listenerFunc: (Int) -> Unit ) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(com.beloushkin.unphotos.R.drawable.placeholder)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .addListener(object: RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let {
                    Palette.from(it.toBitmap(100,100))
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            listenerFunc.invoke(getContrastVersionForColor(getComplementaryColor(intColor)))
                        }
                }
                return false
            }
        })
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

