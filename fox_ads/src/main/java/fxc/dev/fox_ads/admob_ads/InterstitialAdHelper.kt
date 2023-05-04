package fxc.dev.fox_ads.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import fxc.dev.fox_ads.utils.AdsUtils
import fxc.dev.fox_ads.utils.FullScreenLayoutParams
import fxc.dev.fox_ads.view.LoadingView

/**
 * Created by Tam Le on 18/02/2022.
 */

class InterstitialAdHelper
private constructor(
    private var context: Context,
    private var adsInterstitialId: String,
    private var delayShowInterstitialAd: Int
) {
    private val TAG = "Admob"

    var isIntersShow = false

    private val scope = CoroutineScope(Dispatchers.IO)

    private var interstitialAd: InterstitialAd? = null

    private var enableShowInterstitialAd = true

    private var viewGroup: ViewGroup? = null
    private var loadingView: LoadingView? = null
    private var loadingLayoutParams: WindowManager.LayoutParams? = null
    private var loadingViewShowing = false

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    init {
        loadAdmobInterstitialAd()
        createLoadingView()
    }

    fun showInterstitialAd(
        activity: FragmentActivity,
        closeListener: () -> Unit
    ) {
        if (!AdsUtils.canShowAds()) {
            closeListener.invoke()
            return
        }

        viewGroup = activity.window?.decorView?.rootView as? ViewGroup

        if (canShowAdmobInterstitialAd()) {
            try {
                showLoadingView()
                Handler(Looper.getMainLooper()).postDelayed({
                    showAdmobInterstitialAd(activity, closeListener)
                }, 500)
            } catch (e: Exception) {
                closeListener.invoke()
            }
        } else {
            closeListener.invoke()
        }
    }

    private fun canShowAdmobInterstitialAd(): Boolean =
        AdsUtils.canShowAds() &&
                interstitialAd != null && !isIntersShow && enableShowInterstitialAd

    private fun showAdmobInterstitialAd(
        activity: Activity,
        closeListener: () -> Unit
    ) {
        if (canShowAdmobInterstitialAd()) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    isIntersShow = false
                    loadAdmobInterstitialAd()
                    closeListener.invoke()

                    enableShowInterstitialAd = false
                    scope.launch {
                        delay(delayShowInterstitialAd.toLong() * 1000)
                        enableShowInterstitialAd = true
                    }
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    isIntersShow = false
                    loadAdmobInterstitialAd()
                    hideLoadingView()
                    closeListener.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    hideLoadingView()
                    isIntersShow = true
                }
            }
            interstitialAd!!.show(activity)
        } else {
            hideLoadingView()
            closeListener.invoke()
        }
    }

    private fun loadAdmobInterstitialAd() {
        InterstitialAd.load(
            context,
            adsInterstitialId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "InterstitialAds was loaded.")
                    this@InterstitialAdHelper.interstitialAd = interstitialAd
                }
            })
    }

    private fun showLoadingView() {
        if (!loadingViewShowing) {
            loadingViewShowing = true
            viewGroup?.addView(loadingView, loadingLayoutParams)
        }
    }

    private fun hideLoadingView() {
        if (loadingViewShowing) {
            loadingViewShowing = false
            viewGroup?.removeView(loadingView)
        }
    }

    private fun createLoadingView() {
        loadingView = LoadingView(
            context
        )
        loadingLayoutParams = FullScreenLayoutParams.get()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: InterstitialAdHelper? = null
        fun getInstance(
            context: Context,
            adsInterstitialId: String,
            delayShowInterstitialAd: Int
        ): InterstitialAdHelper {
            if (instance == null) {
                instance = InterstitialAdHelper(context, adsInterstitialId, delayShowInterstitialAd)
            }
            return instance!!
        }
    }
}