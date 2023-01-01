package com.weiran.mynowinandroid.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

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

    fun getNewsFromAssets(context: Context, assetsName: String): String {
        val stringBuilder = StringBuilder()
        return try {
            val assetManager: AssetManager = context.assets
            val inputStreamReader = InputStreamReader(assetManager.open(assetsName))
            val bufferedReader = BufferedReader(inputStreamReader)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
            inputStreamReader.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            Log.e(TAG, e.printStackTrace().toString())
            ""
        }
    }

    private const val TAG = "FileUtil"

}