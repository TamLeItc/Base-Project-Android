package fxc.dev.app.navigator

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface Navigator {
    fun navigateToMain(activity: Activity)

    fun navigateToDemo(activity: Activity)
    fun navigateToRoomDemo(fragment: Fragment, type: Int)

    fun navigateToInApp(activity: Activity)
    fun navigateToPolicy(activity: Activity)
    fun navigateToTerm(activity: Activity)
}