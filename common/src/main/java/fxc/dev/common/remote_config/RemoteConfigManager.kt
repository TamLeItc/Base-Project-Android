package fxc.dev.common.remote_config

import android.app.Application

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

interface RemoteConfigManager {
    fun onCreate(application: Application)
    fun startFetchConfig(conCompleted: (RemoteConfigManager) -> Unit)
    fun getString(key: String): String
    fun getLong(key: String): Long
    fun getBoolean(key: String): Boolean
}