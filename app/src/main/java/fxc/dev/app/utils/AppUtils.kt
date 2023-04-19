package fxc.dev.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.DisplayMetrics
import androidx.core.app.ShareCompat
import fxc.dev.app.BuildConfig
import fxc.dev.app.R
import fxc.dev.common.utils.PrefUtils

/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

object AppUtils {

    val isPremium: Boolean
        get() {
            return PrefUtils.isPremium || PrefUtils.isBackDoorUsed
        }

    fun gotoAppInGooglePlay(context: Context) {
        try {
            val url = "market://details?id=${BuildConfig.APPLICATION_ID}"
            context.startActivity(IntentUtils.actionView(url))
        } catch (e: Exception) {
            val url = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            context.startActivity(IntentUtils.actionView(url))
        }
    }

    fun shareApp(activity: Activity) {
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setChooserTitle(activity.getString(R.string.share_app))
            .setText("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            .startChooser();
    }

    //Goto setting
    fun goToSettings(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(IntentUtils.settingApp(), 9999)
    }

    fun goToWirelessSettings(activity: Activity, requestCode: Int) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        activity.startActivityForResult(intent, requestCode)
    }

    fun getDensityDpi(activity: Activity): Int {
        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }
        return outMetrics.densityDpi
    }
}