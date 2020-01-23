package com.beloushkin.unphotos.util


import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beloushkin.unphotos.R


fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        setColorSchemeColors(R.color.colorPrimaryLight)
        start()
    }
}

