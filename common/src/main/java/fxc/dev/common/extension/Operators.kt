package com.fxc.foxcode_framework.extension

/**
 * Created by Thanh Quang on 8/24/21.
 */

fun <T : Any, U : Any> List<T>.joinBy(collection: List<U>, filter: (Pair<T, U>) -> Boolean): List<Pair<T, U?>> = map { t ->
    val filtered = collection.firstOrNull { filter(Pair(t, it)) }
    Pair(t, filtered)
}