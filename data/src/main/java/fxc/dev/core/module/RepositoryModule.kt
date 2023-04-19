package fxc.dev.core.module

import fxc.dev.core.data.repository.LocalRepositoryImp
import fxc.dev.core.data.repository.RemoteRepositoryImp
import fxc.dev.core.domain.repository.LocalRepository
import fxc.dev.core.domain.repository.RemoteRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

val repositoryModule = module {
    singleOf(::RemoteRepositoryImp) { bind<RemoteRepository>() }
    singleOf(::LocalRepositoryImp) { bind<LocalRepository>() }
}