package fxc.dev.foxcode_tracking.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust

/**
 *
 * Created by tamle on 25/05/2023
 *
 */

class TrackingLifecycle : Application.ActivityLifecycleCallbacks {

    fun setRegisterLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    companion object {
        private var instance: TrackingLifecycle? = null
        fun getInstance(): TrackingLifecycle {
            if (instance == null) {
                instance = TrackingLifecycle()
            }
            return instance!!
        }
    }
}