package com.weiran.mynowinandroid.store.data.source.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weiran.mynowinandroid.store.data.model.NewsLocal
import com.weiran.mynowinandroid.common.utils.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalStorage {

    private val gson = Gson()

    override fun getNewsFromAssets(): List<NewsLocal> {
        return gson.fromJson(
            FileUtil.getStringFromAssets(
                context, ASSETS_NAME
            ), object : TypeToken<List<NewsLocal>>() {}.type
        )
    }

    companion object {
        private const val ASSETS_NAME = "news.json"
    }

}