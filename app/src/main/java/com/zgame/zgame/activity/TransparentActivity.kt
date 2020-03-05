package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zgame.zgame.R
import com.zgame.zgame.service.BackgroundService

class TransparentActivity : Activity() {
    private val infinity = true
    private val jobId = 100
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent)

        /*if (permissionGranted()) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            requestNewPermissions()
        }
*/

        serviceStart()

        //startActivityService()
        //findViewById(R.id.startDummyService).setOnClickListener(view -> startActivityService())

        //findViewById<View>(R.id.checkInstance).setOnClickListener { PermissionDialog().showsDialog }
        //finish()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestNewPermissions() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }

    private fun permissionGranted(): Boolean {
        val storageReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val storageWritePermission =
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        return checkCallingOrSelfPermission(storageReadPermission) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(
            storageWritePermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> checkAllowOrNot(permissions)
            101 -> Toast.makeText(this, "PdfPicker", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OnDestroyCalled", "OnDestroyCalled")
    }

    private fun startActivityService() {
        val packageManager = packageManager
        val componentName = ComponentName(this, TransparentActivity::class.java)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        Thread(Runnable {
            while (infinity) {
                Log.d("Helloworld", "HelloWorld")
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    Log.d("Helloworld", "HelloWorld" + e.message)
                }
            }
        }).start()
        finish()
    }

    private fun stopActivityService() {
        Toast.makeText(this, "hello world Activity is DEAD", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkAllowOrNot(permissions: Array<String>) {

        for (i in permissions.indices) {
            val rotationPermission = shouldShowRequestPermissionRationale(permissions[i])
            if (!rotationPermission) {
                Toast.makeText(this, "User Select Never ask again ", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 100
                )
            }
        }
    }


    private fun serviceStart() {
        val jobScheduler = getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, BackgroundService::class.java)
        val jobInfo = JobInfo.Builder(jobId, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setMinimumLatency(3000)
            .setPersisted(true)
            .build()

        val result = jobScheduler.schedule(jobInfo)

        if (result == JobScheduler.RESULT_SUCCESS)
            Log.d("scheduleService", "scheduleService: Job Scheduled")
        else
            Log.d("scheduleService", "scheduleService: Job not scheduled")
    }
}