package com.zgame.zgame

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zgame.zgame.service.BackgroundService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tag = "startService"
    private val jobId = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_job_start.setOnClickListener {
            serviceStart()
        }
        btn_job_end.setOnClickListener {
            serviceEnd()
        }
    }

    private fun serviceStart() {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, BackgroundService::class.java)
        val jobInfo = JobInfo.Builder(jobId, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setMinimumLatency(3000)
            .setPersisted(true)
            .build()

        val result = jobScheduler.schedule(jobInfo)

        if (result == JobScheduler.RESULT_SUCCESS)
            Log.d(tag, "scheduleService: Job Scheduled")
        else
            Log.d(tag, "scheduleService: Job not scheduled")
    }

    private fun serviceEnd() {
        val jobScheduler = this.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(jobId)
        Log.d(tag, "cancelService: job cancelled")
    }
}
