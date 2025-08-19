package com.example.data.di

import com.example.data.datasource.RemoteDataSource
import com.example.data.datasource.RemoteDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    factory<RemoteDataSource> { RemoteDataSourceImpl(get()) }
}