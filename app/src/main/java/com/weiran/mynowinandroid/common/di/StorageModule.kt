package com.weiran.mynowinandroid.common.di

import com.weiran.mynowinandroid.store.data.source.datasource.DataSource
import com.weiran.mynowinandroid.store.data.source.datasource.DataSourceImpl
import com.weiran.mynowinandroid.store.data.source.local.LocalStorage
import com.weiran.mynowinandroid.store.data.source.local.LocalStorageImpl
import com.weiran.mynowinandroid.store.data.source.sp.SpStorage
import com.weiran.mynowinandroid.store.data.source.sp.SpStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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