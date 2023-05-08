package fxc.dev.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *
 * Created by tamle on 07/05/2023
 *
 */

class CoroutineDispatchersImp : CoroutineDispatchers {
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}