package com.beloushkin.unphotos.util

import com.beloushkin.unphotos.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private const val CONNECT_TIMEOUT = 15L
    private const val READ_TIMEOUT = 10L

    private var retrofit: Retrofit? = null

    fun getApiClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val original = chain.request()
                    // adding headers:
                    val request = original.newBuilder()
                        .header("Authorization", "Client-ID ${BuildConfig.ApiKey}")
                        .method(original.method(), original.body())
                        .build()

                    return@Interceptor chain.proceed(request)

                })
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit

    }

}
