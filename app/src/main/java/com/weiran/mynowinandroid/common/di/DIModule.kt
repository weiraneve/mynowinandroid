package com.weiran.mynowinandroid.common.di

import androidx.room.Room
import com.weiran.mynowinandroid.common.utils.NetworkMonitor
import com.weiran.mynowinandroid.domain.NewsUseCase
import com.weiran.mynowinandroid.domain.TopicUseCase
import com.weiran.mynowinandroid.pages.foryou.ForYouViewModel
import com.weiran.mynowinandroid.pages.home.HomeViewModel
import com.weiran.mynowinandroid.pages.interest.InterestViewModel
import com.weiran.mynowinandroid.pages.saved.SavedViewModel
import com.weiran.mynowinandroid.store.data.source.datasource.DataSource
import com.weiran.mynowinandroid.store.data.source.datasource.DataSourceImpl
import com.weiran.mynowinandroid.store.data.source.local.LocalStorage
import com.weiran.mynowinandroid.store.data.source.local.LocalStorageImpl
import com.weiran.mynowinandroid.store.data.source.room.AppDatabase
import com.weiran.mynowinandroid.store.data.source.sp.SpStorage
import com.weiran.mynowinandroid.store.data.source.sp.SpStorageImpl
import com.weiran.mynowinandroid.store.repository.NewsRepository
import com.weiran.mynowinandroid.store.repository.NewsRepositoryImpl
import com.weiran.mynowinandroid.store.repository.TopicRepository
import com.weiran.mynowinandroid.store.repository.TopicRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }

    viewModel { ForYouViewModel(get(), get()) }

    viewModel { SavedViewModel(get()) }

    viewModel { InterestViewModel(get()) }
}

val roomModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DB_NAME).build()
    }
}

val storageModule = module {
    single<DataSource> { DataSourceImpl(get(), get(), Dispatchers.IO) }

    single<LocalStorage> { LocalStorageImpl(get()) }

    single<SpStorage> { SpStorageImpl(get()) }
}

val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get()) }

    single<TopicRepository> { TopicRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    single { NewsUseCase(get()) }

    single { TopicUseCase(get()) }
}

val networkMonitorModule = module {
    single { NetworkMonitor(get()) }
}