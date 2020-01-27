package com.zgame.zgame.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.d
import android.widget.Toast
import com.zgame.zgame.MainActivity

class Reciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        d("makecall","makeCall")
        Toast.makeText(p0,"Reciever Activated",Toast.LENGTH_LONG).show()
        //p0?.startActivity(Intent(p0,MainActivity::class.java))

    }
}