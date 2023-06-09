package fxc.dev.core.data.repository

import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.core.data.source.remote.ApiService
import fxc.dev.core.data.source.remote.dto.UserDTO
import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Tam Le on 30/10/2021.
 */

class RemoteRepositoryImp
constructor(
    private val dispatcher: CoroutineDispatchers,
    private val apiService: ApiService,
) : RemoteRepository {

    override fun getAppConfigs(url: String): Flow<AppConfig> = flow {
        emit(apiService.getAppConfigs(url = url))
    }.flowOn(dispatcher.io)

    override fun getUsers(page: Int): Flow<UserDTO> = flow {
        emit(apiService.getUser(page))
    }.flowOn(dispatcher.io)
}