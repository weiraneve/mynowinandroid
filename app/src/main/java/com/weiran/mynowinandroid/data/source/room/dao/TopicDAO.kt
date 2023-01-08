package com.weiran.mynowinandroid.data.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity


@Dao
interface TopicDAO {

    @Query("SELECT * FROM topic")
    fun getAll(): List<TopicEntity>

    @Query("SELECT * FROM topic WHERE id=:topicID")
    fun getOne(topicID: String): TopicEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(topicEntity: TopicEntity): Long

}