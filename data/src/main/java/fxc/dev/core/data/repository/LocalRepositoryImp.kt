package fxc.dev.core.data.repository

import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.core.data.source.local.LocalDataDao
import fxc.dev.core.domain.model.Post
import fxc.dev.core.domain.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Tam Le on 31/10/2021.
 */

class LocalRepositoryImp
constructor(
    private val dispatchers: CoroutineDispatchers,
    private val localDataDao: LocalDataDao
) : LocalRepository {
    override fun getPost(): Flow<List<Post>> {
        return localDataDao.getPosts()
            .flowOn(dispatchers.io)
    }

    override fun insertPost(post: Post) {
        localDataDao.insert(post)
    }

    override fun deletePost(post: Post) {
        localDataDao.deletePost(post)
    }
}