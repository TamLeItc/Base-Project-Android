package fxc.dev.app.ui.demo

import android.util.Log
import androidx.lifecycle.viewModelScope
import fxc.dev.app.ui.demo_room.HandleUserState
import fxc.dev.base.core.BaseVM
import fxc.dev.core.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

@OptIn(ExperimentalCoroutinesApi::class)
class DemoVM : BaseVM() {

    private var _userState: MutableStateFlow<FetchUserState> = MutableStateFlow(FetchUserState.Init)
    val userState = _userState.asStateFlow()

    private var _handleUserState: MutableSharedFlow<HandleUserState> = MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
    val handleUserState = _handleUserState.asSharedFlow()

    private val _loadUserTriggerS = MutableSharedFlow<Int>(extraBufferCapacity = Int.MAX_VALUE)
    private val _saveUserTriggerS = MutableSharedFlow<User>(extraBufferCapacity = Int.MAX_VALUE)

    private val users = mutableListOf<User>()

    init {
        _loadUserTriggerS
            .flatMapLatest {
                handleFetchUsers(page = it)
            }
            .onEach(_userState::tryEmit)
            .launchIn(viewModelScope)

        _saveUserTriggerS
            .flatMapLatest {
                handleSaveUser(it)
            }
            .onEach(_handleUserState::tryEmit)
            .launchIn(viewModelScope)
    }

    fun fetchUsers(page: Int) = _loadUserTriggerS.tryEmit(page)

    fun saveUser(user: User) = _saveUserTriggerS.tryEmit(user)

    private fun handleFetchUsers(page: Int): Flow<FetchUserState> =
        remoteRepository.getUsers(page)
            .map {
                users.addAll(it.data)
                FetchUserState.Success(
                    data = users,
                    enableLoadMore = it.page < it.total_pages
                ) as FetchUserState
            }
            .onStart { if (page == 0) emit(FetchUserState.Start) }
            .catch { emit(FetchUserState.Failure) }

    private fun handleSaveUser(user: User) =
        localRepository.insert(user)
            .map {
                HandleUserState.Success as HandleUserState
            }
            .catch { HandleUserState.Failure }

}

sealed class FetchUserState {
    object Init : FetchUserState()
    object Start : FetchUserState()
    object Failure : FetchUserState()
    data class Success(val data: List<User>, val enableLoadMore: Boolean) : FetchUserState()
}