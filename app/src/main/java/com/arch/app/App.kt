package com.arch.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.arch.app.assets.R
import com.arch.app.common.utils.doFromSdk
import com.arch.app.common.utils.notificationManager
import com.arch.app.di.mainModule
import com.arch.app.di.networkModule
import com.arch.app.stories.di.storiesModules
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@ExperimentalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
//        if (isDebug) Timber.plant(Timber.DebugTree())
//        else Timber.plant(FirebaseTree())

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                mutableListOf(
                    mainModule,
                    networkModule,
                ).addModules(
                    listOf(
                        storiesModules
                    )
                )
            )
        }
        createNotificationChannels()
    }

    private fun MutableList<Module>.addModules(listOf: List<List<Module>>) =
        apply {
            listOf.forEach { modules ->
                addAll(modules)
            }
        }

    private fun createNotificationChannels() {
        doFromSdk(Build.VERSION_CODES.O) {
            val name = getString(R.string.general_all_notification_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(BuildConfig.CHANNEL_ALL_ID, name, importance)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
