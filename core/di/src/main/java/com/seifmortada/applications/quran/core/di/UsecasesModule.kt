package com.seifmortada.applications.quran.core.di


import com.seifmortada.applications.quran.core.domain.usecase.FetchAyahRecitationUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetAllRecitersUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetAzkarsUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetQuranUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahByIdUseCase
import com.seifmortada.applications.quran.core.domain.usecase.GetSurahRecitationUseCase
import com.seifmortada.applications.quran.core.domain.usecase.settings.GetAppSettingsUseCase
import com.seifmortada.applications.quran.core.domain.usecase.DownloadSurahUseCase
import org.koin.dsl.module

val usecaseModule = module {
    single { GetQuranUseCase(quranRepository = get()) }
    single { GetAzkarsUseCase(zikrRepository = get()) }
    single { FetchAyahRecitationUseCase(surahRepository = get()) }
    single { GetSurahByIdUseCase(quranRepository = get()) }
    single { GetSurahRecitationUseCase(surahRecitationRepository = get()) }
    single { GetAllRecitersUseCase(recitersRepository = get()) }
    single { GetAppSettingsUseCase(settingsRepository = get()) }
    single { DownloadSurahUseCase(downloadRepository = get()) }
}
