package fxc.dev.core.data.source.local

import androidx.room.*
import fxc.dev.core.domain.model.User
import kotlinx.coroutines.flow.Flow


/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

@Dao
interface LocalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM `User`")
    fun getUsers(): Flow<List<User>>

    @Delete
    suspend fun delete(user: User)
}