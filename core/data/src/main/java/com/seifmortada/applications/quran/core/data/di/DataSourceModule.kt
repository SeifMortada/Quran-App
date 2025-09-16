package com.seifmortada.applications.quran.core.data.di

import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSource
import com.seifmortada.applications.quran.core.data.datasource.RemoteDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    factory<RemoteDataSource> { RemoteDataSourceImpl(get()) }
}
