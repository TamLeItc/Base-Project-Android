package fxc.dev.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}