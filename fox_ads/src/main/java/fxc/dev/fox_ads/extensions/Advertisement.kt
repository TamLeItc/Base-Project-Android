package fxc.dev.fox_ads.extensions

import com.google.android.gms.ads.nativead.NativeAd
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.utils.AdsUtils

/**
 * Created by Tam Le on 31/10/2021.
 */

fun <T : Any> List<T>.mergeWithNativeAds(nativeAds: List<NativeAd>, offset: Int = 4): List<Any> {
    if (this.isEmpty() || nativeAds.isEmpty() || !AdsUtils.canShowAds()) {
        return this
    }

    val mergeList: MutableList<Any> = ArrayList()

    val adsSize = this.size / (offset - 1)
    val ads: MutableList<NativeAd> = ArrayList()

    for (index in 0 until (adsSize + 1)) {
        ads.add(nativeAds[index % nativeAds.size])
    }

    var posAds = 0
    var posItem = 0
    for (index in 1 until (adsSize + this.size + 1)) {
        if (index % offset == 0) {
            mergeList.add(ads[posAds++])
        } else {
            mergeList.add(this[posItem++])
        }
    }

    return mergeList
}

fun List<NativeAd>.mergeForListData(data: List<Any>, offset: Int = 4): List<Any> {

    if (data.isEmpty() || this.isEmpty() || !AdsUtils.canShowAds()) {
        return data
    }

    val mergeList: MutableList<Any> = ArrayList()

    val adsSize = data.size / (offset - 1)
    val ads: MutableList<NativeAd> = ArrayList()

    for (index in 0 until (adsSize + 1)) {
        ads.add(this[index % this.size])
    }

    var posAds = 0
    var posItem = 0
    for (index in 1 until (adsSize + data.size + 1)) {
        if (index % offset == 0) {
            mergeList.add(ads[posAds++])
        } else {
            mergeList.add(data[posItem++])
        }
    }

    return mergeList
}