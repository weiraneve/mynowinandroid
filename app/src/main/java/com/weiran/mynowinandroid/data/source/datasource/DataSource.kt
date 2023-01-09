package com.weiran.mynowinandroid.data.source.datasource

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.Topic

interface DataSource {

    fun getTopics(): List<Topic>

    fun saveTopics(topics: List<Topic>)

    fun getNews(): List<News>

    fun saveNews(newsList: List<News>)

    fun updateIsMarkedById(newsId: String)

}