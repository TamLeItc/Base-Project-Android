package fxc.dev.core.data.source.remote

import fxc.dev.core.data.source.remote.dto.UserDTO
import fxc.dev.core.domain.model.AppConfig
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Tam Le on 30/10/2021.
 */

interface ApiService {
    @GET
    suspend fun getAppConfigs(
        @Url url: String
    ): AppConfig

    @GET("users")
    suspend fun getUser(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10,
    ): UserDTO
}