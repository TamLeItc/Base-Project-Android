package fxc.dev.core.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *
 * Created by tamle on 17/04/2023
 *
 */

class AuthInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        builder.header("Accept", "application/json")
        request = builder.build()
        return chain.proceed(request)
    }
}