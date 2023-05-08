package fxc.dev.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 *
 * Created by tamle on 07/05/2023
 *
 */

interface CoroutineDispatchers {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
}
