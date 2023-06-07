package fxc.dev.core.domain.repository

import kotlinx.coroutines.flow.Flow
import fxc.dev.core.domain.model.AppConfig

/**
 *
 * Created by tamle on 31/10/2022
 *
 */
interface RemoteRepository {
    fun getAppConfigs(url: String): Flow<AppConfig>
}