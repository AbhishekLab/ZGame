package com.zgame.zgame

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.zgame.zgame.databinding.ActivityMainBinding
import com.zgame.zgame.fragment.FragmentB
import com.zgame.zgame.service.BackgroundService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val tag = "startService"
    private val jobId = 100
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private var fabRotateAnimation: RotateAnimation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(mBinding.bottomAppBar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        mBinding.bottomAppBar.setOnMenuItemClickListener {  menuItem ->
            menuItem.onNavDestinationSelected(navController)
        }

        mBinding.fab.setImageResource(R.drawable.ic_fab_24dp)



       // mBinding.fab.animation = AnimationUtils.loadAnimation(this, R.anim.fab_rotate)

        /*fabRotateAnimation = RotateAnimation(0F,360F,RotateAnimation.RELATIVE_TO_SELF,0.5F,RotateAnimation.RELATIVE_TO_SELF,0.5F)
        fabRotateAnimation?.duration = 5000
        mBinding.fab.animation = fabRotateAnimation*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
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

    private fun redirectToAddTenantPage() {

    }


}
