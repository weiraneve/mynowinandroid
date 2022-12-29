package com.weiran.mynowinandroid.di

import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.data.source.LocalStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalStorageModule {

    @Binds
    abstract fun bindLocalStorage(localStorageImpl: LocalStorageImpl): LocalStorage

}