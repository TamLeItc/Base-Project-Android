package fxc.dev.common.bus

import android.os.Looper

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface BusProvider {
    fun register(obj: Any)
    fun unregister(obj: Any)
    fun post(event: Any)
}