package fxc.dev.common.utils

import com.chibatching.kotpref.KotprefModel

/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

object PrefUtils : KotprefModel() {
    var language by stringPref("en")
}