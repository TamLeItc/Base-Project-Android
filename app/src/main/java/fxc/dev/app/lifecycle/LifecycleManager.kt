package fxc.dev.app.lifecycle

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import fxc.dev.MainApplication
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_purchase.billing.view.SubscriptionActivity
import fxc.dev.fox_purchase.billing.view.webview.WebViewActivity

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
        if (currentActivity is SubscriptionActivity || currentActivity is WebViewActivity) {
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
    }

    override fun onActivityPaused(activity: Activity) {}

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