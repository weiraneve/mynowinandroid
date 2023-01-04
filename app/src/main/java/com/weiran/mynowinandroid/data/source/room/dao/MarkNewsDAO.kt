package com.weiran.mynowinandroid.data.source.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weiran.mynowinandroid.data.source.room.model.MarkedNewsEntity


@Dao
interface MarkedNewsDAO {

    @Query("SELECT * FROM markedNews")
    fun getAll(): List<MarkedNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(markedNewsEntity: MarkedNewsEntity): Long

    @Delete
    fun remove(markedNewsEntity: MarkedNewsEntity)

}