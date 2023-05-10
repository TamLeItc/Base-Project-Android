package fxc.dev.app.helper

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust
import fxc.dev.MainApplication
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.app.ui.purchase.PurchaseActivity
import fxc.dev.app.ui.webview.WebViewActivity

/**
 * Created by Tam Le on 17/06/2022.
 */

class LifecycleManager(
    var adsHelper: AdsHelper
) : LifecycleObserver, Application.ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null

    fun initialize() {
        MainApplication.instance.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (currentActivity is PurchaseActivity || currentActivity is WebViewActivity) {
            return
        }
        adsHelper.showAppOpenAd(currentActivity, false)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        Adjust.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        bundle: Bundle
    ) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }
}