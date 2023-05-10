package fxc.dev.common.remote_config

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import fxc.dev.common.R

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

class RemoteConfigManagerImp(application: Application) : RemoteConfigManager {

    private var config: FirebaseRemoteConfig? = null

    override fun onCreate(application: Application) {
        config = FirebaseRemoteConfig.getInstance()
        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .build()
        config?.setConfigSettingsAsync(configSettings)

        config?.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun startFetchConfig(conCompleted: (RemoteConfigManager) -> Unit) {
        config?.fetchAndActivate()
            ?.addOnCompleteListener { task ->
                conCompleted(this)
            }
    }

    override fun getString(key: String) = config?.getString(key) ?: ""

    override fun getLong(key: String) = config?.getLong(key) ?: 0L

    override fun getBoolean(key: String) = config?.getBoolean(key) ?: false
}