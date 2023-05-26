package fxc.dev.foxcode_tracking.module

import fxc.dev.foxcode_tracking.event_tracking.EventTracking
import fxc.dev.foxcode_tracking.event_tracking.EventTrackingImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind

/**
 *
 * Created by tamle on 26/05/2023
 *
 */

val trackingModule = module {
    singleOf(::EventTrackingImp) { bind<EventTracking>() }
}