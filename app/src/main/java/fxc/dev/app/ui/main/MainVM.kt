package fxc.dev.app.ui.main

import androidx.lifecycle.viewModelScope
import fxc.dev.app.BuildConfig
import fxc.dev.app.constants.Constants
import fxc.dev.base.core.BaseVM
import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class MainVM : BaseVM() {

    val appConfigState: StateFlow<AppConfigState>

    init {
        appConfigState = fetchAppConfig()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                AppConfigState.Init
            )
    }

    private fun fetchAppConfig() =
        remoteRepository.getAppConfigs(Constants.appConfigUrl)
            .map { AppConfigState.Success(it) as AppConfigState }
            .onStart { emit(AppConfigState.Start) }
            .retry(retries = 1)
            .catch { emit(AppConfigState.Failure) }

}

sealed class AppConfigState {
    object Init : AppConfigState()
    object Start : AppConfigState()
    object Failure : AppConfigState()
    data class Success(val appConfig: AppConfig) : AppConfigState()
}