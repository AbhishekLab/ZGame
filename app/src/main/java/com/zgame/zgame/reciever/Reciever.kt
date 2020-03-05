package com.zgame.zgame.reciever

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.app.ActivityManager
import android.util.Log.d
import android.content.ComponentName
import android.app.ActivityManager.RunningTaskInfo
import android.content.pm.PackageManager
import com.zgame.zgame.activity.TransparentActivity


class Reciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        val number = p1!!.getStringExtra(Intent.EXTRA_PHONE_NUMBER)

        /*if(t.localClassName == "TransparentActivity"){
            d("CLasssss", t.localClassName)
        }else{
            d("CLasssss  Else", t.localClassName)
        }*/


        Toast.makeText(p0, number.toString(), Toast.LENGTH_LONG).show()

       /* val storageReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val storageWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if(p0!!.checkCallingOrSelfPermission(storageReadPermission) == PackageManager.PERMISSION_GRANTED && p0.checkCallingOrSelfPermission(storageWritePermission) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(p0,"PermissionGranted",Toast.LENGTH_LONG).show()
        }else{
            p0.startActivity(Intent(p0,TransparentActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }*/


        /*if (isRunning(p0!!)) {
            Toast.makeText(p0, number.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(p0, "no activity srarted", Toast.LENGTH_LONG).show()
        }
        p0.startActivity(Intent(p0,TransparentActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*/

    }

    private fun isRunning(ctx: Context): Boolean {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)

        for (task in tasks) {
            if (ctx.packageName.equals(ctx.packageName, ignoreCase = true))
                Toast.makeText(ctx, ctx.packageName.toString(), Toast.LENGTH_LONG).show()
            return true
        }

        return false
    }
}