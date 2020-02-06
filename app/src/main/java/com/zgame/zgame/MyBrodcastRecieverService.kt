package com.zgame.zgame

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log.d


class MyBrodcastRecieverService : Service() {


    private var br_ScreenOffReceiver: BroadcastReceiver? = null


    override fun onBind(p0: Intent?): IBinder? {

        d("OnBind","onbindCall")

        return null
    }

    override fun onCreate() {

        d("OnBind","OnCreateCall")

        registerScreenOffReceiver()

    }

    private fun registerScreenOffReceiver() {
        br_ScreenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                d("Action", "ACTION_SCREEN_OFF")
            }
        }

        val filter = IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL)
        registerReceiver(br_ScreenOffReceiver, filter)
    }

}
