package fxc.dev.app.state

import fxc.dev.core.domain.model.AppConfig

/**
 *
 * Created by tamle on 10/06/2023
 *
 */

sealed class AppConfigState {
 object Init : AppConfigState()
 object Start : AppConfigState()
 object Failure : AppConfigState()
 data class Success(val appConfig: AppConfig) : AppConfigState()
}