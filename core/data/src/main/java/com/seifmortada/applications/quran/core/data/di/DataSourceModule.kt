package com.seifmortada.applications.quran.core.data.di

import com.example.data.datasource.RemoteDataSourceImpl
import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    factory<RemoteDataSource> { RemoteDataSourceImpl(get()) }
}
