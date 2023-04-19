package fxc.dev.core.module

import android.content.Context
import fxc.dev.common.BuildConfig
import fxc.dev.core.data.source.remote.ApiService
import fxc.dev.core.utils.AuthInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Thanh Quang on 18/07/2022.
 */
private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
private const val NETWORK_TIMEOUT = 30L

val networkModule = module {
    factory { AuthInterceptor() }
    factory { GsonConverterFactory.create() }
    factory { provideLoggingInterceptor() }
    factory { provideOkHttpClient(get(), get(), get()) }
    single { provideService<ApiService>(BASE_URL, get(), get()) }
}

private fun provideOkHttpClient(
    context: Context,
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val myCache = Cache(context.cacheDir, (5 * 1024 * 1024).toLong())
    val builder = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(authInterceptor)
        .callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
    return builder.build()
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return logger
}

private inline fun <reified T> provideService(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()
        .create(T::class.java)
}