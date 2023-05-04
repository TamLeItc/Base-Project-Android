package fxc.dev.common.module

import android.os.Handler
import android.os.Looper
import com.squareup.otto.Bus
import fxc.dev.common.bus.BusProvider
import fxc.dev.common.bus.BusProviderImp
import fxc.dev.common.wrapper.AppContextWrapper
import fxc.dev.common.wrapper.AppContextWrapperImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

val commonModule = module {
    single { Bus() }
    single { Handler(Looper.getMainLooper()) }
    singleOf(::AppContextWrapperImp) { bind<AppContextWrapper>() }
    singleOf(::BusProviderImp) { bind<BusProvider>() }
}