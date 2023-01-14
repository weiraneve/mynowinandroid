package com.weiran.mynowinandroid.repository.interfaces

import com.weiran.mynowinandroid.data.model.News

interface NewsRepository {

    suspend fun getNews(): List<News>

    suspend fun updateIsMarkedById(newsId: String): Long

}