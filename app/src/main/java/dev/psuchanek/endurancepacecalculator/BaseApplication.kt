package dev.psuchanek.endurancepacecalculator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
    }

    private fun plantTimber() {
        Timber.plant(Timber.DebugTree())
    }
}