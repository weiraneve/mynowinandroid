package com.weiran.mynowinandroid.store.data.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weiran.mynowinandroid.store.data.source.room.model.NewsEntity


@Dao
interface NewsDAO {

    @Query("SELECT * FROM news")
    fun getAll(): List<NewsEntity>

    @Query("SELECT * FROM news WHERE id=:newsId")
    fun getOne(newsId: Long): NewsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsEntity: NewsEntity): Long

}