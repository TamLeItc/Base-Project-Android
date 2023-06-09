package fxc.dev.app.navigator

import android.app.Activity
import android.content.Intent
import fxc.dev.app.R
import fxc.dev.app.ui.demo.DemoActivity
import fxc.dev.app.ui.demo_room.DemoRoomActivity
import fxc.dev.app.ui.main.MainActivity
import fxc.dev.app.ui.purchase.PurchaseActivity
import fxc.dev.app.ui.webview.WebViewActivity
import fxc.dev.fox_purchase.utils.PurchaseUtils

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

    override fun navigateToDemo(activity: Activity) {
        val intent = DemoActivity.getIntent(activity)
        activity.startActivity(intent)
    }

    override fun navigateToDemoRoom(activity: Activity) {
        val intent = DemoRoomActivity.getIntent(activity)
        activity.startActivity(intent)
    }

    override fun navigateToInApp(activity: Activity) {
        if (PurchaseUtils.isPremium) {
            return
        }
        val intent = PurchaseActivity.getIntent(activity)
        activity.startActivity(intent)
    }

    override fun navigateToPolicy(activity: Activity) {
        val intent = WebViewActivity.getIntent(
            activity,
            activity.getString(R.string.privacy_policy_url)
        )
        activity.startActivity(intent)
    }

    override fun navigateToTerm(activity: Activity) {
        val intent = WebViewActivity.getIntent(
            activity,
            activity.getString(R.string.term_of_use_url)
        )
        activity.startActivity(intent)
    }
}