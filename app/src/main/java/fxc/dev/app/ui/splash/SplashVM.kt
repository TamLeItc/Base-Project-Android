package fxc.dev.app.ui.splash

import android.os.Build
import fxc.dev.app.helper.lifecycle.LifecycleManager
import fxc.dev.base.core.BaseVM
import fxc.dev.common.extension.flow.interval
import fxc.dev.fox_ads.admob_ads.AppOpenAdHelper
import fxc.dev.fox_ads.utils.AdsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _launchAppState: MutableStateFlow<LauncherState> = MutableStateFlow(LauncherState.Init)
    val launchAppState  = _launchAppState.asStateFlow()

    private var job: Job? = null
    private val mainScope = CoroutineScope(dispatchers.main)

    private var openAdsListener: AppOpenAdHelper.AppOpenAdListener? = object :
        AppOpenAdHelper.AppOpenAdListener {
        override fun onAdShowed() {
            onCleared()
        }

        override fun onAdClosed() {
            _launchAppState.value = LauncherState.GoToMain
        }
    }

    fun fetchData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            _launchAppState.value = LauncherState.RequireUpdateSDK
            return
        }

        mainScope.launch {
            delay(1000)

            if (AdsUtils.canShowAds()) {
                fetchOpenAds()
                startWaitingTimer()
            } else {
                _launchAppState.value = LauncherState.GoToMain
            }
        }
    }

    private fun fetchOpenAds() {
        if (!AdsUtils.canShowAds()) {
            _launchAppState.value = LauncherState.GoToMain
            onCleared()
        } else {
            adsHelper.showAppOpenAd(
                activity = LifecycleManager.getInstance().getCurrentActivity(),
                enableShowAfterFetchAd = true,
                listener = openAdsListener
            )
        }
    }

    private fun startWaitingTimer() {
        job = interval(delay = 1, unit = TimeUnit.SECONDS)
            .onEach {
                if (it >= 15) {
                    _launchAppState.value = LauncherState.GoToMain
                    onCleared()
                }
            }
            .launchIn(mainScope)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        mainScope.cancel()
    }
}

sealed class LauncherState {
    object Init : LauncherState()
    object RequireUpdateSDK : LauncherState()
    object GoToMain : LauncherState()
}