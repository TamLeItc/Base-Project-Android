package fxc.dev.fox_ads.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.*
import fxc.dev.fox_ads.constants.BannerSize

/**
 * Created by Tam Le on 16/06/2022.
 */

class BannerAdHelper
private constructor() {

    private val adRequest = AdRequest.Builder().build()

    fun addBanner(
        activity: Activity,
        viewParent: ViewGroup,
        adUnitId: String,
        bannerSize: BannerSize = BannerSize.SMART,
        onAdLoaded: (() -> Unit)? = null,
        onAdFailedToLoad: (() -> Unit)? = null,
    ) {
        val adView = AdView(activity)
        adView.setAdSize(getRealAdSize(activity, bannerSize))
        adView.adUnitId = adUnitId
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                val parent = adView.parent
                if (parent != null) {
                    (parent as ViewGroup).removeView(adView)
                }
                viewParent.addView(adView)
                onAdLoaded?.invoke()
                Log.d("BannerAds", "Admob: onAdLoaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                onAdFailedToLoad?.invoke()
                viewParent.visibility = View.GONE
                Log.d("BannerAds", "Admob: $p0")
            }
        }
        adView.loadAd(adRequest)
    }

    private fun getRealAdSize(activity: Activity, bannerSize: BannerSize): AdSize {
        return when(bannerSize) {
            BannerSize.SMART -> {
                getAdapterAdSize(activity)
            }
            BannerSize.RECTANGLE -> {
                AdSize.MEDIUM_RECTANGLE
            }
            BannerSize.LARGE -> {
                AdSize.LARGE_BANNER
            }
        }
    }

    private fun getAdapterAdSize(activity: Activity): AdSize {
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            activity,
            (outMetrics.widthPixels.toFloat() / outMetrics.density).toInt()
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: BannerAdHelper? = null
        fun getInstance(): BannerAdHelper {
            if (instance == null) {
                instance = BannerAdHelper()
            }
            return instance!!
        }
    }
}