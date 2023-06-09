package fxc.dev.core.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fxc.dev.core.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by tamle on 31/10/2022
 *
 */

interface LocalRepository {
    fun insert(user: User): Flow<Unit>

    fun getUsers(): Flow<List<User>>

    fun delete(user: User): Flow<Unit>
}