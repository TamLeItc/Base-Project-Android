package fxc.dev.fox_purchase.model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by Thanh Quang on 8/23/21.
 */
data class IAPInfo(
    @DrawableRes val icon: Int,
    @StringRes val text: Int
)