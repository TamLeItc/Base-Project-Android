package fxc.dev.core.data.source.local

import androidx.room.*
import fxc.dev.core.data.source.local.converter.Converters
import fxc.dev.core.domain.model.Demo


/**
 *
 * Created by TamLe on 7/5/20.
 *
 */

@Database(
    entities = [
        Demo::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDao(): LocalDataDao
}