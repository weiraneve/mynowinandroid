package com.weiran.mynowinandroid.utils

import android.content.Context
import androidx.annotation.RawRes
import java.io.IOException

object FileUtil {

    fun getStringFromRaw(context: Context, @RawRes id: Int): String {
        var str = ""
        return try {
            val stream = context.resources.openRawResource(id)
            str = stream.bufferedReader().use { it.readText() }
            stream.close()
            str
        } catch (_: IOException) {
            str
        }
    }

}