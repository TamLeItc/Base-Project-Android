package fxc.dev.app.helper.lifecycle

import android.app.Activity

/**
 *
 * Created by tamle on 10/05/2023
 *
 */

interface LifecycleManager {
    fun initialize()
    fun getCurrentActivity(): Activity?
}