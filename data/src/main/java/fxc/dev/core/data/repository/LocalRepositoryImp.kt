package fxc.dev.core.data.repository

import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.core.data.source.local.LocalDataDao
import fxc.dev.core.domain.model.User
import fxc.dev.core.domain.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Tam Le on 31/10/2021.
 */

class LocalRepositoryImp
constructor(
    private val dispatcher: CoroutineDispatchers,
    private val localDataDao: LocalDataDao
) : LocalRepository {

    override fun insert(user: User): Flow<Unit> = flow {
        emit(localDataDao.insert(user))
    }.flowOn(dispatcher.io)

    override fun getUsers(): Flow<List<User>> {
        return localDataDao.getUsers()
            .flowOn(dispatcher.io)
    }

    override fun delete(user: User): Flow<Unit> = flow {
        emit(localDataDao.delete(user))
    }.flowOn(dispatcher.io)

}