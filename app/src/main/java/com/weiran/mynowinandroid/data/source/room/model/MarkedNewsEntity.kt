package com.weiran.mynowinandroid.data.source.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markedNews")
class MarkedNewsEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 1

}