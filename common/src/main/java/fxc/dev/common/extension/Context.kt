package fxc.dev.common.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * Created by Tam Le on 19/11/2021.
 */

fun Context.resourceColor(@ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, theme)
    } else {
        resources.getColor(res)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.resourceDrawable(@DrawableRes res: Int): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getDrawable(res, theme)
    } else {
        resources.getDrawable(res)
    }
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}