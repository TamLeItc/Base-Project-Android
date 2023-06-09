package fxc.dev.app.state

/**
 *
 * Created by tamle on 09/06/2023
 *
 */

sealed class FetchApiState<out T> {
 object Init : FetchApiState<Nothing>()
 object Start : FetchApiState<Nothing>()
 data class Failure(val throwable: Throwable?) : FetchApiState<Nothing>()
 data class Success<T>(val data: T, val enableLoadMore: Boolean = false) : FetchApiState<T>()
}