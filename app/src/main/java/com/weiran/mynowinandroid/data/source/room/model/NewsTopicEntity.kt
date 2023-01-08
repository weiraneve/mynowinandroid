package com.weiran.mynowinandroid.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "news_topic")
class NewsTopicEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "news_id")
    var newsId: String = ""

    @ColumnInfo(name = "topic_id")
    var topicId: String = ""

}