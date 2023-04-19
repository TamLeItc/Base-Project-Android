package fxc.dev.app.navigator

import android.app.Activity
import android.content.Intent
import fxc.dev.app.ui.main.MainActivity

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class NavigatorImp : Navigator {
    override fun navigateToMain(activity: Activity) {
        val intent = MainActivity.getIntent(activity)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        activity.startActivity(intent)
        activity.finish()
    }
}