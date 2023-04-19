package fxc.dev.fox_ads.admob_ads

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NativeAdHelper
private constructor(
    context: Context,
    adsNativeId: String
) {

    private val TAG = "NativeAdsManager"

    private val _nativeAdsS = MutableStateFlow<List<NativeAd>>(emptyList())
    val nativeAdsS = _nativeAdsS.asStateFlow()

    private lateinit var adLoader: AdLoader

    private val ads: MutableList<NativeAd>

    private var loadCount = 0
    private var currentDelay = 1000 // milliseconds;
    private val numberOfAds = 5

    private val maxRetryCount = 3
    private val delayFactor = 2

    private val adsSubject2 = MutableLiveData<List<NativeAd>>()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        ads = ArrayList(numberOfAds)

        adLoader = AdLoader.Builder(context, adsNativeId)
            .forNativeAd { NativeAd ->
                Log.d(
                    TAG,
                    "onNativeAdLoaded NativeAd=" + NativeAd + ",adLoader.isLoading()=" + adLoader.isLoading
                )
                Log.d("forNativeAd", "${ads.size}")
                ads.add(NativeAd)
                if (adLoader.isLoading) {
                    ads.add(NativeAd)
                } else {
                    _nativeAdsS.tryEmit(ads)
                    adsSubject2.value = ads
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.e(TAG, "onAdFailedToLoad errorCode=${error.code},loadCount=$loadCount")
                    if (loadCount <= maxRetryCount) {
                        currentDelay *= delayFactor
                        scope.launch {
                            delay(currentDelay.toLong())
                            loadAds()
                        }
                        Log.e(TAG, "Schedule loadAds after $currentDelay ms")
                    }
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .build()
            )
            .build()

        loadAds()
    }

    private fun loadAds() {
        loadCount++
        adLoader.loadAds(
            AdRequest.Builder()
                .build(),
            numberOfAds
        )
        Log.d(TAG, "Call loadAds loadCount=$loadCount")
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: NativeAdHelper? = null
        fun getInstance(
            context: Context,
            adsNativeId: String
        ): NativeAdHelper {
            if (instance == null) {
                instance = NativeAdHelper(context, adsNativeId)
            }
            return instance!!
        }
    }
}
