package fxc.dev.fox_ads.interfaces

import android.widget.FrameLayout
import fxc.dev.fox_ads.constants.BannerSize

/**
 *
 * Created by tamle on 10/05/2023
 *
 */

interface IAdsHelper {
    fun loadBannerAds(parentView: FrameLayout, adSize: BannerSize = BannerSize.SMART)
}