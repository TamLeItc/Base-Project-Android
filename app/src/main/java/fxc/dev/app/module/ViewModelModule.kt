package fxc.dev.app.module

import fxc.dev.app.ui.main.MainVM
import fxc.dev.app.ui.splash.SplashVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

val viewModelModule = module {
    viewModelOf(::MainVM)
    viewModelOf(::SplashVM)
}