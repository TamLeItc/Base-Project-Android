package fxc.dev.app.ui.demo.grid

import androidx.lifecycle.viewModelScope
import fxc.dev.app.state.FetchApiState
import fxc.dev.app.state.SimpleHandleState
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
 * Created by tamle on 09/06/2023
 *
 */

@OptIn(ExperimentalCoroutinesApi::class)
class DemoGridVM : BaseVM() {

    private var _userState: MutableStateFlow<FetchApiState<List<Any>>> = MutableStateFlow(FetchApiState.Init)
    val userState = _userState.asStateFlow()

    private var _handleUserState: MutableSharedFlow<SimpleHandleState> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
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

        fetchUsers(0)
    }

    fun fetchUsers(page: Int) = _loadUserTriggerS.tryEmit(page)

    fun saveUser(user: User) = _saveUserTriggerS.tryEmit(user)

    private fun handleFetchUsers(page: Int): Flow<FetchApiState<List<Any>>> =
        remoteRepository.getUsers(page)
            .map {
                users.addAll(it.data)
                FetchApiState.Success(
                    data = users,
                    enableLoadMore = it.page < it.total_pages
                ) as FetchApiState<List<Any>>
            }
            .onStart { if (page == 0) emit(FetchApiState.Start) }
            .catch { emit(FetchApiState.Failure(it)) }

    private fun handleSaveUser(user: User) =
        localRepository.insert(user)
            .map {
                SimpleHandleState.Success as SimpleHandleState
            }
            .catch { SimpleHandleState.Failure }

}