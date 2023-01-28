package com.weiran.mynowinandroid.store.data.source.sp

import android.content.Context
import com.weiran.mynowinandroid.common.utils.SharedPreferenceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SpStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SpStorage {

    override fun writeFlag(key: String, value: Boolean) {
        SharedPreferenceUtil.writeBoolean(
            context, SharedPreferenceUtil.SHARED_PREFERENCE_FILE, key, value
        )
    }

    override fun readFlag(key: String) = SharedPreferenceUtil.readBoolean(
        context, SharedPreferenceUtil.SHARED_PREFERENCE_FILE, key, true
    )
}