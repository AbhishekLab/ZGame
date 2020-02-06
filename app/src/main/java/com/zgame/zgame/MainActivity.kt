package com.zgame.zgame

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import com.google.firebase.auth.FirebaseAuth
import com.zgame.zgame.camera.GalleryActivity
import com.zgame.zgame.databinding.ActivityMainBinding
import com.zgame.zgame.reciever.Reciever
import com.zgame.zgame.service.BackgroundService

class MainActivity : AppCompatActivity() {

    private val tag = "startService"
    private val jobId = 100
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(mBinding.bottomAppBar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        checkAuthInstance()

        mBinding.bottomAppBar.setNavigationOnClickListener {

            if (mAuth.currentUser != null) {
                mAuth.signOut()
            }
        }

        mBinding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            menuItem.onNavDestinationSelected(navController)
        }

        mBinding.fab.setImageResource(R.drawable.ic_fab_24dp)

        mBinding.fab.setOnClickListener {
            fabClick()
        }
    }

    fun checkAuthInstance(): Boolean {
        return mAuth.currentUser != null
    }


    private fun fabClick() {
        startActivity(Intent(this, GalleryActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    fun startBroadcastServer() {
        val service = Intent(this, Reciever::class.java)
        startService(service)
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
