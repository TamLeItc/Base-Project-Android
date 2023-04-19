package fxc.dev.fox_ads.utils

import fxc.dev.common.utils.PrefUtils

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

object AdsUtils {
    fun canShowAds(): Boolean =
        !PrefUtils.isPremium && !PrefUtils.isBackDoorUsed
}