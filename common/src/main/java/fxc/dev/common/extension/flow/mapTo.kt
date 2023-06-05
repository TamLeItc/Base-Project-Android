package fxc.dev.common.extension.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun <T, R> Flow<T>.mapTo(v: R): Flow<R> = transform {
    return@transform emit(v)
}