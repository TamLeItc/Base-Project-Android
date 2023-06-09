package fxc.dev.app.state

/**
 *
 * Created by tamle on 10/06/2023
 *
 */

sealed class SimpleHandleState {
    object Success : SimpleHandleState()
    object Failure : SimpleHandleState()
}