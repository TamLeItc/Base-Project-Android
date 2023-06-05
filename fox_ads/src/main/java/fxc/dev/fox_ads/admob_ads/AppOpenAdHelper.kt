package fxc.dev.fox_ads.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import fxc.dev.fox_ads.utils.AdsUtils
import java.util.*

//
// Created by TamLe on 12/25/20.
//

class AppOpenAdHelper
private constructor(
    private var application: Application,
    private var adsOpenId: String
) : Application.ActivityLifecycleCallbacks {

    interface AppOpenAdListener {
        fun onAdShowed()
        fun onAdClosed()
    }

    private val TAG = "AppOpenAdManager"

    private var isShowingAd = false

    private var appOpenAd: AppOpenAd? = null
    private var loadTime: Long = 0
    private var adListener: AppOpenAdListener? = null

    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    fun showAd(
        activity: Activity?,
        enableShowAfterFetchAd: Boolean = false,
        listener: AppOpenAdListener? = null
    ) {
        this.adListener = listener
        if (!AdsUtils.canShowAds()) {
            listener?.onAdClosed()
            return
        } else {
            showAdIfAvailable(activity, enableShowAfterFetchAd)
        }
    }

    private fun showAdIfAvailable(
        activity: Activity?,
        enableShowAfterFetchAd: Boolean
    ) {
        if (canShowAppOpenAd()) {
            Log.d(TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        adListener?.onAdClosed()
                        fetchAd(activity, false)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, adError.message)
                        adListener?.onAdClosed()
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        adListener?.onAdShowed()
                    }
                }
            if (activity != null) {
                appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
                appOpenAd!!.show(activity)
            }
        } else {
            Log.d(TAG, "Can not show ad.")
            fetchAd(activity, enableShowAfterFetchAd)
        }
    }

    private fun fetchAd(activity: Activity?, enableShowAfterFetchAd: Boolean) {
        if (isAdAvailable) {
            Log.e(TAG, "AdNoLoaded")
            return
        }

        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                super.onAdLoaded(ad)
                appOpenAd = ad
                loadTime = Date().time
                Log.e(TAG, "AdLoaded")

                if (enableShowAfterFetchAd) {
                    showAdIfAvailable(activity, false)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.e(
                    TAG,
                    "onAppOpenAdFailedToLoad: " + loadAdError.message
                )
            }
        }
        val request = adRequest
        AppOpenAd.load(
            application, adsOpenId, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }

    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4.toLong()
    }

    private fun canShowAppOpenAd(): Boolean {
        return appOpenAd != null && isAdAvailable && !isShowingAd
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: AppOpenAdHelper? = null
        fun getInstance(
            application: Application,
            adsOpenId: String
        ): AppOpenAdHelper {
            if (instance == null) {
                instance = AppOpenAdHelper(application, adsOpenId)
            }
            return instance!!
        }
    }
}