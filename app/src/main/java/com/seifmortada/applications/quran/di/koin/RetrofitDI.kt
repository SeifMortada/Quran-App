package com.seifmortada.applications.quran.di.koin

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.seifmortada.applications.quran.BuildConfig
import com.seifmortada.applications.quran.MyApplication.Companion.getContext
import com.seifmortada.applications.quran.data.rest.apis.QuranApi
import com.seifmortada.applications.quran.data.rest.apis.RecitersApi
import com.seifmortada.applications.quran.utils.NetworkUtils.isNetworkAvailable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.cache.CacheInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = BuildConfig.BASE_URL
const val cacheSize = (5 * 1024 * 1024).toLong()  // 5MB cache size

fun provideCache(): Cache {
    return Cache(getContext().cacheDir, cacheSize)
}

fun provideCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        var request = chain.request()
        // Check network connectivity (assuming there's a function to check it)
        if (isNetworkAvailable(getContext()).not()) {
            // If there is no network, force the cache
            request = request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24
                ) // 1 day
                .build()
        }
        chain.proceed(request)
    }
}

fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    val cache = provideCache()
    val cacheInterceptor = provideCacheInterceptor()

    val timeoutSeconds = 120L

    return OkHttpClient.Builder()
        .cache(cache)  // Set cache
        .addInterceptor(interceptor)  // Log responses
        .addInterceptor(cacheInterceptor)  // Handle cache
        .addNetworkInterceptor(provideNetworkCacheInterceptor())  // Caching for online state
        .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .build()
}

fun provideNetworkCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        val response = chain.proceed(chain.request())
        // Customize cache-control for online requests
        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.MINUTES)  // Cache data for 5 minutes when online
            .build()

        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}

fun provideGson(): Gson = GsonBuilder().create()

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(provideGson()))
        .build()
}

fun provideQuranApi(): QuranApi =
    provideRetrofit()
        .create(QuranApi::class.java)

fun provideRecitersApi(): RecitersApi =
    provideRetrofit().create(RecitersApi::class.java)