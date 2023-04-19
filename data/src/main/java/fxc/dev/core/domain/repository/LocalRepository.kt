package fxc.dev.core.domain.repository

import fxc.dev.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by tamle on 31/10/2022
 *
 */

interface LocalRepository {
    fun getPost(): Flow<List<Post>>
    fun insertPost(post: Post)
    fun deletePost(post: Post)
}