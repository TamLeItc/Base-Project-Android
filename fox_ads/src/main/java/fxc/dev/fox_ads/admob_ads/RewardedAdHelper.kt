package fxc.dev.fox_ads.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Created by Tam Le on 18/02/2022.
 */

class RewardedAdHelper
private constructor(
    private var context: Context,
    private var adsRewardId: String,
) {

    private val TAG = "Admob"

    private val scope = CoroutineScope(Dispatchers.IO)

    private var rewardedAd: RewardedAd? = null
    private var isRewardShow = false

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    init {
        loadRewardedAd()
    }

    fun showRewardAd(
        activity: Activity,
        closeListener: () -> Unit,
        earnedRewardListener: (RewardItem) -> Unit
    ) {
        if (canShowRewardedAd()) {
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    // don't show the ad a second time.
                    isRewardShow = false
                    loadRewardedAd()
                    closeListener.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    isRewardShow = false
                    loadRewardedAd()
                    closeListener.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    isRewardShow = true
                }
            }
            rewardedAd?.show(activity) {
                earnedRewardListener.invoke(it)
            }
        }
    }

    private fun canShowRewardedAd(): Boolean {
        return rewardedAd != null && !isRewardShow
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            context,
            adsRewardId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(rewardedAd: LoadAdError) {
                    super.onAdFailedToLoad(rewardedAd)
                    Log.d(TAG, rewardedAd.message)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Log.d(TAG, "RewardedAd was loaded.")
                    this@RewardedAdHelper.rewardedAd = rewardedAd
                }
            }
        )
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RewardedAdHelper? = null
        fun getInstance(
            context: Context,
            adsRewardId: String
        ): RewardedAdHelper {
            if (instance == null) {
                instance = RewardedAdHelper(context, adsRewardId)
            }
            return instance!!
        }
    }
}