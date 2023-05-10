package fxc.dev.fox_ads.utils

import fxc.dev.fox_purchase.utils.PurchaseUtils

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

object AdsUtils {
    fun canShowAds(): Boolean = !PurchaseUtils.isPremium
}