package fxc.dev.foxcode_tracking.remote_config

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import fxc.dev.common.R
import fxc.dev.dji_drone.common.remote_config.RemoteConfigKey

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

object RemoteConfigManager {

    private const val TAG = "RemoteConfigManager"

    fun onCreate(application: Application) {
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .build()
        config.setConfigSettingsAsync(configSettings)

        config.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun startFetchConfig(conCompleted: () -> Unit) {
        FirebaseRemoteConfig.getInstance().fetchAndActivate()
            .addOnCompleteListener { task ->
                conCompleted()
                Log.d(TAG, getString(RemoteConfigKey.ADJUST_APP_TOKEN))
                Log.d(TAG, getString(RemoteConfigKey.ADJUST_PURCHASE_TOKEN))
            }
    }

    fun getString(key: String) = FirebaseRemoteConfig.getInstance().getString(key)

    fun getLong(key: String) = FirebaseRemoteConfig.getInstance().getLong(key)

    fun getBoolean(key: String) = FirebaseRemoteConfig.getInstance().getBoolean(key)
}