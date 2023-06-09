package fxc.dev.app.ui.demo.room

import androidx.lifecycle.viewModelScope
import fxc.dev.app.state.LoadUserState
import fxc.dev.app.state.SimpleHandleState
import fxc.dev.base.core.BaseVM
import fxc.dev.core.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 *
 * Created by tamle on 08/06/2023
 *
 */

@OptIn(ExperimentalCoroutinesApi::class)
class DemoRoomVM : BaseVM() {

    val userState : StateFlow<LoadUserState>

    private var _handleUserState: MutableSharedFlow<SimpleHandleState> = MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
    val handleUserState = _handleUserState.asSharedFlow()

    private val _loadUserTriggerS = MutableSharedFlow<Unit>(extraBufferCapacity = Int.MAX_VALUE)
    private val _deleteUserTriggerS = MutableSharedFlow<User>(extraBufferCapacity = Int.MAX_VALUE)

    init {
        userState = handleFetchUsers()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                LoadUserState.Init
            )

        _deleteUserTriggerS
            .flatMapLatest {
                handleDeleteUser(it)
            }
            .onEach(_handleUserState::tryEmit)
            .launchIn(viewModelScope)
    }

    fun deleteUser(user: User) = _deleteUserTriggerS.tryEmit(user)

    private fun handleFetchUsers(): Flow<LoadUserState> =
        localRepository.getUsers()
            .map {
                LoadUserState.Success(it) as LoadUserState
            }
            .catch { emit(LoadUserState.Failure) }

    private fun handleDeleteUser(user: User) =
        localRepository.delete(user)
            .map {
                SimpleHandleState.Success as SimpleHandleState
            }
            .catch { SimpleHandleState.Failure }
}