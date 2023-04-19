package fxc.dev.common.bus

import android.os.Handler
import android.os.Looper
import com.squareup.otto.Bus

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class BusProviderImp(
    private val bus: Bus,
    private val handler: Handler
): BusProvider {
    override fun register(obj: Any) {
        bus.register(obj)
    }

    override fun unregister(obj: Any) {
        try {
            bus.unregister(obj)
        } catch (e: IllegalArgumentException) {
            throw e
        }
    }

    override fun post(event: Any) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            bus.post(event)
        } else {
            handler.post(AnonymousClass1(event))
        }
    }

    private inner class AnonymousClass1(val any: Any) : Runnable {
        override fun run() {
            this@BusProviderImp.bus.post(this.any)
        }
    }
}