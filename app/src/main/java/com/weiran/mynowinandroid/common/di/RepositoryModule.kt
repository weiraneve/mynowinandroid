package com.weiran.mynowinandroid.common.di

import com.weiran.mynowinandroid.store.repository.NewsRepository
import com.weiran.mynowinandroid.store.repository.NewsRepositoryImpl
import com.weiran.mynowinandroid.store.repository.TopicRepository
import com.weiran.mynowinandroid.store.repository.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindTopicRepository(topicRepositoryImpl: TopicRepositoryImpl): TopicRepository
}