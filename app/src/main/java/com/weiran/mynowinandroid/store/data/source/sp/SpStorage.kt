package com.weiran.mynowinandroid.store.data.source.sp

interface SpStorage {

    fun writeFlag(key: String, value: Boolean)

    fun readFlag(key: String): Boolean

}