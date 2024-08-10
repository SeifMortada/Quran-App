package com.seifmortada.applications.quran.di.koin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.seifmortada.applications.quran.data.remote.QuranApiService
import com.seifmortada.applications.quran.utils.SajdaDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideBaseUrl(): String = "http://api.alquran.cloud/v1/"

fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val timeoutSeconds = 120L // Set my desired timeout in seconds
    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
        .build()
}

fun provideGson(): Gson = GsonBuilder().registerTypeAdapter(Any::class.java, SajdaDeserializer())
    .setLenient().create()

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(provideBaseUrl())
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(provideGson()))
        .build()
}

fun provideQuranApiService(): QuranApiService =
    provideRetrofit()
        .create(QuranApiService::class.java)