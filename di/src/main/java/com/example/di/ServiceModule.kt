package com.example.di

import com.example.data.di.databaseModule
import com.example.data.di.networkModule
import org.koin.dsl.module

val serviceModule = module {
    includes(networkModule)
    includes(databaseModule)
}