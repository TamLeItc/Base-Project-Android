package fxc.dev.app.helper.lifecycle

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

class LifecycleManagerImp(
    var adsHelper: AdsHelper
): Application.ActivityLifecycleCallbacks, LifecycleManager, DefaultLifecycleObserver {

    private var currentActivity: Activity? = null

    override fun initialize() {
        MainApplication.instance.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
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