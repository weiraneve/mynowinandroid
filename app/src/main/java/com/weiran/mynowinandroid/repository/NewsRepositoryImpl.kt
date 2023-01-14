package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.repository.interfaces.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val dataSource: DataSource) : NewsRepository {

    override suspend fun getNews(): List<News> = dataSource.getNews()

    override suspend fun updateIsMarkedById(newsId: String) = dataSource.updateIsMarkedById(newsId)

}