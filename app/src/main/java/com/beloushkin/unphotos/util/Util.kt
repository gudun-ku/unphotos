package com.beloushkin.unphotos.util


import android.content.Context
import android.graphics.Color
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

fun getComplementaryColor(colorToInvert: Int): Int {
    val hsv = FloatArray(3)
    Color.RGBToHSV(
        Color.red(colorToInvert), Color.green(colorToInvert),
        Color.blue(colorToInvert), hsv
    )
    hsv[0] = (hsv[0] + 180) % 360
    return Color.HSVToColor(hsv)
}

