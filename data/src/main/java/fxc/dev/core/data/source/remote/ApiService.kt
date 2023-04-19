package fxc.dev.core.data.source.remote

import fxc.dev.core.domain.model.AppConfig
import fxc.dev.core.domain.model.Post
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Tam Le on 30/10/2021.
 */

interface ApiService {
    @GET("posts")
    suspend fun getPost(): List<Post>

    @GET
    suspend fun getAppConfigs(
        appId: String,
        @Url url: String = "https://adsnetwork-api.romancenovelx.com/api/v1/configs/${appId}"
    ): AppConfig
}