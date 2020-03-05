package com.zgame.zgame

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import com.google.firebase.auth.FirebaseAuth
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.camera.GalleryActivity
import com.zgame.zgame.databinding.ActivityMainBinding
import com.zgame.zgame.service.BackgroundService
import com.zgame.zgame.utils.Constant

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val tag = "startService"
    private val jobId = 100
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            Constant.STORAGE_PERMISSION -> callUtils()
        }
    }

    override fun contentView() = R.layout.activity_main

    override fun initUI(binding: ActivityMainBinding) {
        mBinding = binding
        mAuth = FirebaseAuth.getInstance()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    private fun fabClick() {
        startActivity(Intent(this, GalleryActivity::class.java))
    }


    fun checkAuthInstance(): Boolean {
        return mAuth.currentUser != null
    }

    fun permission() {
        requestAppPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            R.string.permission_text,
            Constant.STORAGE_PERMISSION
        )
    }


    /*fun startBroadcastServer() {
        val service = Intent(this, Reciever::class.java)
        startService(service)
    }*/

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

    private fun callUtils() {
        showToast("CalllUtils")
    }


}
