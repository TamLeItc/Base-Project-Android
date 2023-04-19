package fxc.dev.app.module

import fxc.dev.app.navigator.Navigator
import fxc.dev.app.navigator.NavigatorImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 19/04/2023
 *
 */

val navigatorModule = module {
    singleOf(::NavigatorImp) { bind<Navigator>() }
}