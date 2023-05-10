package fxc.dev.app.module

import fxc.dev.app.helper.LifecycleManager
import org.koin.dsl.module

/**
 *
 * Created by tamle on 19/04/2023
 *
 */

val lifecycleModule = module {
    single { LifecycleManager(get()) }
}
