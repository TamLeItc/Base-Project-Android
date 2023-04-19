package fxc.dev.app.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import fxc.dev.app.BuildConfig

/**
 * Created by Tam Le on 13/07/2022.
 */

object IntentUtils {

    fun settingApp(): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun actionView(url: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}