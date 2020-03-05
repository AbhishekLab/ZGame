package com.zgame.zgame.base

import android.app.Application

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferanceRepository.init(this)
    }
}