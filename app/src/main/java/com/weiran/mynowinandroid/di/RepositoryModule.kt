package com.weiran.mynowinandroid.di

import com.weiran.mynowinandroid.repository.NewsRepositoryImpl
import com.weiran.mynowinandroid.repository.TopicRepositoryImpl
import com.weiran.mynowinandroid.repository.interfaces.NewsRepository
import com.weiran.mynowinandroid.repository.interfaces.TopicRepository
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