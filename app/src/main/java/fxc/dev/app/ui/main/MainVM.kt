package fxc.dev.app.ui.main

import androidx.lifecycle.viewModelScope
import fxc.dev.app.constants.Constants
import fxc.dev.app.state.AppConfigState
import fxc.dev.base.core.BaseVM
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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