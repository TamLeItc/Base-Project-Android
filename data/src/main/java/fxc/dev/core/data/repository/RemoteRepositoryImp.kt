package fxc.dev.core.data.repository

import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.model.Post
import fxc.dev.core.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import fxc.dev.core.data.source.remote.ApiService

/**
 * Created by Tam Le on 30/10/2021.
 */

class RemoteRepositoryImp
constructor(
    private val apiService: ApiService
) : RemoteRepository {

    override fun getPost(): Flow<List<Post>> = flow {
        emit(apiService.getPost())
    }.flowOn(Dispatchers.IO)

    override fun getAppConfigs(url: String): Flow<AppConfig> = flow {
        emit(apiService.getAppConfigs(url = url))
    }.flowOn(Dispatchers.IO)
}