package com.weiran.mynowinandroid.di

import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.data.source.datasource.DataSourceImpl
import com.weiran.mynowinandroid.data.source.local.LocalStorage
import com.weiran.mynowinandroid.data.source.local.LocalStorageImpl
import com.weiran.mynowinandroid.data.source.sp.SpStorage
import com.weiran.mynowinandroid.data.source.sp.SpStorageImpl
import com.weiran.mynowinandroid.repository.interfaces.NewsRepository
import com.weiran.mynowinandroid.repository.NewsRepositoryImpl
import com.weiran.mynowinandroid.repository.interfaces.TopicRepository
import com.weiran.mynowinandroid.repository.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun bindLocalStorage(localStorageImpl: LocalStorageImpl): LocalStorage

    @Binds
    abstract fun bindSpStorage(spStorageImpl: SpStorageImpl): SpStorage

    @Binds
    abstract fun bindDataSource(dataSourceImpl: DataSourceImpl): DataSource

}