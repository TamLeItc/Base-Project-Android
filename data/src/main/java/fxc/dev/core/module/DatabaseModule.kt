package fxc.dev.core.module

import android.app.Application
import androidx.room.Room
import fxc.dev.core.data.source.local.LocalDataDao
import fxc.dev.core.data.source.local.LocalDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 *
 * Created by tamle on 17/04/2023
 *
 */

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}

private fun provideDatabase(application: Application): LocalDatabase {
    return Room.databaseBuilder(application, LocalDatabase::class.java, "db")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideCountriesDao(database: LocalDatabase): LocalDataDao {
    return  database.localDao()
}