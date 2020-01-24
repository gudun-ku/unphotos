package com.beloushkin.unphotos.util


import android.content.Context
import android.graphics.Color
import android.os.Build

import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beloushkin.unphotos.R


fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setColorSchemeColors(context.resources.getColor(R.color.colorProgress, context.theme)
                ,context.resources.getColor(R.color.colorPrimaryLight, context.theme))
        } else {
            @Suppress("DEPRECATION")
            setColorSchemeColors(context.resources.getColor(R.color.colorProgress)
                ,context.resources.getColor(R.color.colorPrimaryLight))
        }
        start()
    }
}

fun getComplementaryColor(colorToInvert: Int): Int {
    val hsv = FloatArray(3)
    Color.RGBToHSV(
        Color.red(colorToInvert), Color.green(colorToInvert),
        Color.blue(colorToInvert), hsv
    )
    hsv[0] = (hsv[0] + 240) % 360
    return Color.HSVToColor(hsv)
}

fun getContrastVersionForColor(color: Int): Int {
    val hsv = FloatArray(3)
    Color.RGBToHSV(
        Color.red(color), Color.green(color), Color.blue(color),
        hsv
    )
    if (hsv[2] < 0.5) {
        hsv[2] = 0.7f
    } else {
        hsv[2] = 0.3f
    }
    hsv[1] = hsv[1] * 0.5f
    return Color.HSVToColor(hsv)
}

