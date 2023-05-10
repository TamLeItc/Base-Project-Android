package fxc.dev.app.module

import fxc.dev.app.helper.event_tracking.EventTracking
import fxc.dev.app.helper.event_tracking.EventTrackingImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

val eventTrackingModule = module {
    singleOf(::EventTrackingImp) { bind<EventTracking>() }
}