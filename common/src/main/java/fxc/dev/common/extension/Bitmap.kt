package fxc.dev.common.extension

import android.graphics.Bitmap

/**
 * Created by TamLe on 10/22/21.
 */

fun Bitmap.resize(maxSize: Int): Bitmap {
    var width: Int = this.width
    var height: Int = this.height

    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(this, width, height, true)
}