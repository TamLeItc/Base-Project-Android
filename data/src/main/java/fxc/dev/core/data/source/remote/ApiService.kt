package fxc.dev.core.data.source.remote

import fxc.dev.core.domain.model.AppConfig
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Tam Le on 30/10/2021.
 */

interface ApiService {
    @GET
    suspend fun getAppConfigs(
        @Url url: String
    ): AppConfig
}