package fxc.dev.common.utils

import com.chibatching.kotpref.KotprefModel

/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

object PrefUtils : KotprefModel() {
    var isPremium by booleanPref(false)
    var isBackDoorUsed by booleanPref(false)
    var language by stringPref("en")
}