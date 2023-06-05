package fxc.dev.fox_ads

import android.app.Activity
import android.app.Application
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import fxc.dev.common.extension.gone
import fxc.dev.fox_ads.admob_ads.*
import kotlinx.coroutines.flow.*
import fxc.dev.fox_ads.constants.BannerSize
import fxc.dev.fox_ads.utils.AdsUtils

/**
 * Created by Tam Le on 21/02/2022.
 */

class AdsHelper {
    private var appOpenAd: AppOpenAdHelper? = null
    private var bannerAd: BannerAdHelper? = null
    private var rewardedAd: RewardedAdHelper? = null
    private var interstitialAd: InterstitialAdHelper? = null
    private var nativeAd: NativeAdHelper? = null

    private var timeSafeDelayClick = 1000L
    private var lastHandleShowAds = 0L

    fun initialize(
        application: Application,
        delayShowInterstitialAd: Int = 45,
        adsOpenId: String = "",
        adsRewardId: String = "",
        adsInterstitialId: String = "",
        adsNativeId: String = ""
    ) {
        MobileAds.initialize(application)
        appOpenAd = AppOpenAdHelper.getInstance(application, adsOpenId)
        bannerAd = BannerAdHelper.getInstance()
        rewardedAd = RewardedAdHelper.getInstance(application, adsRewardId)
        interstitialAd = InterstitialAdHelper.getInstance(
            application,
            adsInterstitialId,
            delayShowInterstitialAd
        )
        nativeAd = NativeAdHelper.getInstance(application, adsNativeId)
    }

    fun addBanner(
        activity: Activity,
        viewParent: FrameLayout,
        adSize: BannerSize,
        adUnitId: String,
        onAdLoaded: (() -> Unit)? = null,
        onAdFailedToLoad: (() -> Unit)? = null
    ) {
        if (!AdsUtils.canShowAds()) {
            viewParent.gone()
            return
        }
        bannerAd?.addBanner(
            activity,
            viewParent,
            adUnitId,
            adSize,
            onAdLoaded,
            onAdFailedToLoad
        )
    }

    fun showInterstitialAd(
        activity: FragmentActivity,
        onAdsClosed: () -> Unit
    ) {
        if (System.currentTimeMillis() - lastHandleShowAds < timeSafeDelayClick || !AdsUtils.canShowAds()) {
            onAdsClosed.invoke()
            return
        }
        lastHandleShowAds = System.currentTimeMillis()
        interstitialAd?.showInterstitialAd(activity, onAdsClosed)
    }

    fun showAppOpenAd(
        activity: Activity?,
        enableShowAfterFetchAd: Boolean,
        listener: AppOpenAdHelper.AppOpenAdListener? = null
    ) {
        if (interstitialAd?.isIntersShow == true) {
            return
        }
        if (appOpenAd == null) {
            listener?.onAdClosed()
            return
        }
        appOpenAd?.showAd(
            activity,
            enableShowAfterFetchAd,
            listener
        )
    }

    fun getNativeAdFlow(): StateFlow<List<NativeAd>> {
        return if (nativeAd != null) {
            nativeAd!!.nativeAdsS
        } else {
            MutableStateFlow(emptyList())
        }
    }

    companion object {
        private var instance: AdsHelper? = null
        fun getInstance(): AdsHelper {
            if (instance == null){
                instance = AdsHelper()
            }
            return instance!!
        }
    }
}