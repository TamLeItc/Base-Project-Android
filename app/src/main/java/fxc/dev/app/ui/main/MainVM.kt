package fxc.dev.app.ui.main

import androidx.lifecycle.viewModelScope
import fxc.dev.app.BuildConfig
import fxc.dev.base.core.BaseVM
import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class MainVM : BaseVM() {

    private var _appConfigState = MutableStateFlow<AppConfigState>(AppConfigState.Init)
    val appConfigState: StateFlow<AppConfigState>
        get() = _appConfigState

    fun fetchAppConfigs() {
        val url = "https://adsnetwork-api.romancenovelx.com/api/v1/configs/${BuildConfig.APPLICATION_ID}}"
        remoteRepository.getAppConfigs(url)
            .map { AppConfigState.Success(it) as AppConfigState }
            .onStart { emit(AppConfigState.Start) }
            .retry(retries = 2)
            .catch { emit(AppConfigState.Failure) }
            .onEach {
                _appConfigState.emit(it)
            }
            .launchIn(viewModelScope)
    }

}

sealed class AppConfigState {
    object Init : AppConfigState()
    object Start : AppConfigState()
    object Failure : AppConfigState()
    data class Success(val appConfig: AppConfig) : AppConfigState()
}