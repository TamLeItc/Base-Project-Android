package fxc.dev.app.state

import fxc.dev.core.domain.model.User

/**
 *
 * Created by tamle on 10/06/2023
 *
 */

sealed class LoadUserState {
 object Init : LoadUserState()
 object Failure : LoadUserState()
 data class Success(val data: List<User>) : LoadUserState()
}