package fxc.dev.app.helper.lifecycle

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import fxc.dev.app.MainApplication
import fxc.dev.app.ui.purchase.PurchaseActivity
import fxc.dev.app.ui.webview.WebViewActivity
import fxc.dev.fox_ads.AdsHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Created by Tam Le on 17/06/2022.
 */

class LifecycleManager : Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver,
    KoinComponent {

    private var currentActivity: Activity? = null

    fun initialize() {
        MainApplication.instance.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (currentActivity is PurchaseActivity || currentActivity is WebViewActivity) {
            return
        }
        AdsHelper.getInstance().showAppOpenAd(currentActivity, false)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {

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

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: LifecycleManager? = null
        fun getInstance(): LifecycleManager {
            if (instance == null) {
                instance = LifecycleManager()
            }
            return instance!!
        }
    }
}
