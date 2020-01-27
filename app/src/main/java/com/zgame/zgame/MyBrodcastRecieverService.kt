package com.zgame.zgame

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.content.IntentFilter
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MyBrodcastRecieverService : Service() {


    private var br_ScreenOffReceiver: BroadcastReceiver? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        registerScreenOffReceiver()

    }

    private fun registerScreenOffReceiver() {
        br_ScreenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("Action", "ACTION_SCREEN_OFF")
            }
        }


        val filter = IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL)
        registerReceiver(br_ScreenOffReceiver, filter)
    }

}
