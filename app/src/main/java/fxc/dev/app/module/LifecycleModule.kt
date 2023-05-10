package fxc.dev.app.module

import fxc.dev.app.helper.lifecycle.LifecycleManager
import fxc.dev.app.helper.lifecycle.LifecycleManagerImp
import fxc.dev.app.navigator.Navigator
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 19/04/2023
 *
 */

val lifecycleModule = module {
    singleOf(::LifecycleManagerImp) { bind<LifecycleManager>() }
}
