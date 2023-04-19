package fxc.dev.core.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Tam Le on 25/02/2022.
 */

data class AppConfig(
    @SerializedName("version")
    val version: String,

    @SerializedName("storeAppId")
    val storeAppId: String,

    @SerializedName("requireUpdate")
    val requireUpdate: Boolean,

    @SerializedName("isSuspend")
    val isSuspend: Boolean,

    @SerializedName("newApp")
    val newApp: String
)