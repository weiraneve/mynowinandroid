package com.weiran.mynowinandroid.store.data.source.local

import com.weiran.mynowinandroid.store.data.model.NewsLocal

interface LocalStorage {

    fun getNewsFromAssets(): List<NewsLocal>

}