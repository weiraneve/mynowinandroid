package com.weiran.mynowinandroid.common.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object FileUtil {

    fun getStringFromAssets(context: Context, assetsName: String): String {
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