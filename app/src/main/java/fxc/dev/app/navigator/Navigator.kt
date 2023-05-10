package fxc.dev.app.navigator

import android.app.Activity

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface Navigator {
    fun navigateToMain(activity: Activity)

    fun navigateToInApp(activity: Activity)
    fun navigateToPolicy(activity: Activity)
    fun navigateToTerm(activity: Activity)
}