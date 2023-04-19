package fxc.dev.core.domain.repository

import kotlinx.coroutines.flow.Flow
import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.model.Post

/**
 *
 * Created by tamle on 31/10/2022
 *
 */
interface RemoteRepository {
    fun getPost(): Flow<List<Post>>
    fun getAppConfigs(appId: String): Flow<AppConfig>
}