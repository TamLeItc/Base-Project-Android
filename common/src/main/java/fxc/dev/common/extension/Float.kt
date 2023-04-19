package fxc.dev.common.extension

import android.content.Context
import android.util.TypedValue

//
// Created by TamLe on 12/23/20.
//

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}