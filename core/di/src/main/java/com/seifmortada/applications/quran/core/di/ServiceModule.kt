package com.seifmortada.applications.quran.core.di

import com.seifmortada.applications.quran.core.data.di.dataSourceModule
import com.seifmortada.applications.quran.core.data.di.databaseModule
import com.seifmortada.applications.quran.core.data.di.networkModule
import org.koin.dsl.module

val serviceModule = module {
    includes(networkModule)
    includes(databaseModule)
    includes(dataSourceModule)
}
