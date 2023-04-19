package fxc.dev.common.dispatcher

import kotlinx.coroutines.Dispatchers

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class CoroutineDispatcherImp : CoroutineDispatchers {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
}