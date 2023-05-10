package fxc.dev.common.extension

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import java.util.concurrent.TimeUnit

/**
 * Created by Thanh Quang on 5/20/21.
 */
fun timer(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Flow<Unit> {
    return flow {
        delay(unit.toMillis(kotlin.math.abs(delay)))
        emit(Unit)
    }
}

fun <T, R> Flow<T>.mapTo(v: R): Flow<R> = transform {
    return@transform emit(v)
}

fun interval(
    initialDelay: Long = 0L,
    delay: Long,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    initialCount: Long = 0L
): Flow<Long> {
    require(initialDelay >= 0) { "Expected non-negative delay, but has $initialDelay ms" }
    require(delay >= 0) { "Expected non-negative delay, but has $delay ms" }
    return flow {
        delay(unit.toMillis(initialDelay))

        var count = initialCount
        while (true) {
            emit(count++)
            delay(unit.toMillis(delay))
        }
    }
}

fun <T> concat(first: Flow<T>, second: Flow<T>): Flow<T> {
    return flow {
        first.collect { emit(it) }
        second.collect { emit(it) }
    }
}

private object NULL {
    @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
    inline fun <T> unbox(v: Any?): T = if (this === v) null as T else v as T
}

public fun <A, B, R> Flow<A>.withLatestFrom(
    other: Flow<B>,
    transform: suspend (A, B) -> R
): Flow<R> {
    return flow {
        coroutineScope {
            val otherValues = Channel<Any>(Channel.CONFLATED)
            launch(start = CoroutineStart.UNDISPATCHED) {
                other.collect {
                    return@collect otherValues.send(it ?: NULL)
                }
            }

            var lastValue: Any? = null
            collect { value ->
                otherValues
                    .tryReceive()
                    .onSuccess { lastValue = it }

                emit(
                    transform(
                        value,
                        NULL.unbox(lastValue ?: return@collect)
                    ),
                )
            }
        }
    }
}

public fun <T> Flow<List<T>>.filterNotEmpty(): Flow<List<T>> = transform { value ->
    if (value.isNotEmpty()) return@transform emit(value)
}

@Suppress("NOTHING_TO_INLINE")
public inline fun <A, B> Flow<A>.withLatestFrom(other: Flow<B>): Flow<Pair<A, B>> =
    withLatestFrom(other) { a, b -> a to b }