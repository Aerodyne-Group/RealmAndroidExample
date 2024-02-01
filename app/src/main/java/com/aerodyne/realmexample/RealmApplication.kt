package com.aerodyne.realmexample

import android.app.Application

/**
 *
 * Created by Ganesan Gopal on 30/01/2024.
 */
class RealmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            e.printStackTrace()
        }
    }
}