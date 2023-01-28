package com.weiran.mynowinandroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.weiran.mynowinandroid.common.di.networkMonitorModule
import com.weiran.mynowinandroid.common.di.repositoryModule
import com.weiran.mynowinandroid.common.di.roomModule
import com.weiran.mynowinandroid.common.di.storageModule
import com.weiran.mynowinandroid.common.di.useCaseModule
import com.weiran.mynowinandroid.common.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@MainApplication)
            modules(
                listOf(
                    repositoryModule,
                    storageModule,
                    useCaseModule,
                    roomModule,
                    viewModelModule,
                    networkMonitorModule
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}