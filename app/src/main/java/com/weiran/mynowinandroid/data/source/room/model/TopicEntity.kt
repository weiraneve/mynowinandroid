package com.weiran.mynowinandroid.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic")
class TopicEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 1

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "selected")
    var selected: Boolean = false

    @ColumnInfo(name = "image_url")
    var imageUrl: String = ""

}