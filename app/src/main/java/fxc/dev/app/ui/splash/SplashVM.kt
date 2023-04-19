package fxc.dev.app.ui.splash

import android.os.Build
import fxc.dev.app.lifecycle.LifecycleManager
import fxc.dev.base.core.BaseVM
import fxc.dev.common.extension.interval
import fxc.dev.fox_ads.admob_ads.AppOpenAdHelper
import fxc.dev.fox_ads.utils.AdsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class SplashVM : BaseVM() {

    var querySplash: MutableStateFlow<SplashState> = MutableStateFlow(SplashState.Init)

    private val lifecycleManager: LifecycleManager by inject()

    private var job: Job? = null
    private val mainScope = CoroutineScope(dispatchers.main)

    private var openAdsListener: AppOpenAdHelper.AppOpenAdListener? = object :
        AppOpenAdHelper.AppOpenAdListener {
        override fun onAdShowed() {
            clearJob()
        }

        override fun onAdClosed() {
            clearJob()
            mainScope.launch {
                delay(200)
                querySplash.value = SplashState.GoToMain
            }
        }
    }

    fun fetchData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            querySplash.value = SplashState.RequireUpdateSDK
            return
        } else if (adsHelper.appOpenAd == null) {
            querySplash.value = SplashState.GoToMain
            clearJob()
            return
        }

        mainScope.launch {
            delay(1000)

            if (AdsUtils.canShowAds()) {
                fetchOpenAds()

                job = interval(delay = 1, unit = TimeUnit.SECONDS)
                    .onEach {
                        if (it >= 15 && !adsHelper.appOpenAd!!.isShowingAd) {
                            querySplash.value = SplashState.GoToMain
                            clearJob()
                        }
                    }
                    .launchIn(mainScope)
            } else {
                querySplash.value = SplashState.GoToMain
            }
        }
    }

    private fun fetchOpenAds() {
        if (!AdsUtils.canShowAds()) {
            querySplash.value = SplashState.GoToMain
            clearJob()
        } else {
            adsHelper.appOpenAd?.showAd(
                activity = lifecycleManager.currentActivity,
                enableShowAfterFetchAd = true,
                listener = openAdsListener
            )
        }
    }

    private fun clearJob() {
        onCleared()
        job?.cancel()
    }
}

sealed class SplashState {
    object Init : SplashState()
    object RequireUpdateSDK : SplashState()
    object GoToMain : SplashState()
}