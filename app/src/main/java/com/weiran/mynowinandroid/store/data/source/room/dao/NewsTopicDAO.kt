package com.weiran.mynowinandroid.store.data.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weiran.mynowinandroid.store.data.source.room.model.NewsTopicEntity

@Dao
interface NewsTopicDAO {

    @Query("SELECT * FROM news_topic")
    fun getAll(): List<NewsTopicEntity>

    @Query("SELECT * FROM news_topic WHERE news_id=:newsId ")
    fun getByNewsId(newsId: String): List<NewsTopicEntity>

    @Insert
    fun insert(newsTopicEntity: NewsTopicEntity): Long

}