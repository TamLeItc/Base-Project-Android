package fxc.dev.fox_ads

import android.app.Activity
import android.app.Application
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.nativead.NativeAd
import fxc.dev.fox_ads.admob_ads.AppOpenAdHelper
import fxc.dev.fox_ads.constants.BannerSize
import kotlinx.coroutines.flow.StateFlow

interface AdsHelper {
    fun initialize(
        delayShowInterstitialAd: Int = 45,
        adsOpenId: String = "",
        adsBannerId: String = "",
        adsRewardId: String = "",
        adsInterstitialId: String = "",
        adsNativeId: String = ""
    )

    fun addBanner(
        activity: Activity,
        viewParent: FrameLayout,
        adSize: BannerSize,
        onAdLoaded: (() -> Unit)? = null,
        onAdFailedToLoad: (() -> Unit)? = null
    )

    fun showInterstitialAd(
        activity: FragmentActivity,
        onAdsClosed: () -> Unit
    )
    fun showAppOpenAd(
        activity: Activity?,
        enableShowAfterFetchAd: Boolean = false,
        listener: AppOpenAdHelper.AppOpenAdListener? = null
    )

    fun getNativeAdFlow(): StateFlow<List<NativeAd>>
}