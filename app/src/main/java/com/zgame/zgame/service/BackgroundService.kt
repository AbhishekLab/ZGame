package com.zgame.zgame.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Looper
import android.util.Log

class BackgroundService : JobService() {

    private val tag = "Service"
    private var mSuccess = true
    private var isJobCancelled = true

    override fun onStartJob(p0: JobParameters?): Boolean {
        setInfiniteLoop()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        isJobCancelled = false
        return isJobCancelled
    }


    private fun setInfiniteLoop(){
        Thread {
            var i = 0
            Looper.prepare()
            while (mSuccess) {
                if (!isJobCancelled) {
                    Log.d(tag, "onStartJob: Job Cancelled")
                    return@Thread
                }
                Log.d(tag, "run: Download Progress: " + (i + 1))

                Thread.sleep(3000)

                i++
            }

            Looper.loop()
        }.start()
    }
}