package fxc.dev.core.data.source.local

import androidx.room.*
import fxc.dev.core.domain.model.Post
import kotlinx.coroutines.flow.Flow


/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

@Dao
interface LocalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Query("SELECT * FROM `Post`")
    fun getPosts(): Flow<List<Post>>

    @Delete
    fun deletePost(post: Post)
}