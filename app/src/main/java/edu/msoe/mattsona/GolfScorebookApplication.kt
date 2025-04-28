package edu.msoe.mattsona

import android.app.Application

class GolfScorebookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GolfRepository.initialize(this)
    }
}