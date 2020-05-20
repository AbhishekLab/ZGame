package com.zgame.zgame

import android.Manifest
import android.app.AlertDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.util.Log.e
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.activity.PostImageActivity
import com.zgame.zgame.adapter.DrawerItemAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.databinding.ActivityMainBinding
import com.zgame.zgame.model.DrawerListModel
import com.zgame.zgame.service.BackgroundService
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.reservoir_key
import kotlinx.android.synthetic.main.content_drawer.view.*
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val tag = "startService"
    private val jobId = 100
    private lateinit var mBinding: ActivityMainBinding
    private var navController: NavController? = null
    private var gallery: TextView? = null
    lateinit var drawerAdapters: DrawerItemAdapter
    private var alertDialog: AlertDialog? = null


    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            Constant.STORAGE_PERMISSION -> callUtils()
        }
    }

    override fun contentView() = R.layout.activity_main


    override fun initUI(binding: ActivityMainBinding) {
        mBinding = binding
        mAuth = FirebaseAuth.getInstance()

        drawerAdapters = DrawerItemAdapter(this, DrawerListModel.drawerRecyclerDataWithIcons(this))
        mBinding.layoutDrawer.recyclerDrawer.adapter = drawerAdapters

        setSupportActionBar(mBinding.bottomAppBar)
        navController = findNavController(this, R.id.nav_host_fragment)
        mBinding.fab.setImageResource(R.drawable.ic_fab_24dp)
        mBinding.fab.setOnClickListener {
            fabClick()
        }

        mBinding.layoutDrawer.txtLogin.setOnClickListener{
            closeDrawer()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        mBinding.bottomAppBar.setNavigationOnClickListener {
            mBinding.dlLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (navController?.currentDestination?.label == "CustomerGalleryFragment" && item.toString() == "Home" || navController?.currentDestination?.label == "UserProfileFragment" &&
            item.toString() == "Profile"
        ) {
            false
        } else {
            item.onNavDestinationSelected(navController!!)
            true
        }

    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this)
        val layoutView: View = layoutInflater.inflate(R.layout.dialog_log_out, null)
        builder.setView(layoutView)
        alertDialog = builder.create()
        alertDialog?.show()

        val positiveButton = layoutView.findViewById<Button>(R.id.btn_ok)
        val negativeButton = layoutView.findViewById<Button>(R.id.btn_cancel)
        builder.setCancelable(true)
        positiveButton.setOnClickListener {
            PreferanceRepository.logout()
            mAuth.signOut()
            try {
                Reservoir.delete(reservoir_key)
            } catch (e: IOException) {
            }
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            alertDialog?.dismiss()
        }
        negativeButton.setOnClickListener {
            alertDialog?.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    private fun fabClick() {
        if(mAuth.currentUser!=null){
            startActivity(Intent(this, PostImageActivity::class.java))
        }else{
            showToast("Please login to enable this feature")
        }
        //startActivity(Intent(this, GalleryActivity::class.java))
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
        //showToast("CalllUtils")
    }

    fun setUserImage(userName: String, userImageUrl: String) {
        if(mAuth.currentUser!=null){
            mBinding.layoutDrawer.tvName.visibility = View.VISIBLE
            mBinding.layoutDrawer.tvCompanyName.visibility = View.VISIBLE
            mBinding.layoutDrawer.txtLogin.visibility = View.INVISIBLE
            if (userImageUrl != "null" && userImageUrl != "") {
                Glide.with(this)
                    .load(userImageUrl).apply(
                        RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder).circleCrop()
                    ).into(mBinding.layoutDrawer.ivUserpic)
            } else {
                Glide.with(this).load(R.drawable.ic_white_profile_place_holder).into(mBinding.layoutDrawer.ivUserpic)
            }
            mBinding.layoutDrawer.tvName.text = userName

        }else{
            mBinding.layoutDrawer.txtLogin.visibility = View.VISIBLE
            mBinding.layoutDrawer.tvName.visibility = View.INVISIBLE
            mBinding.layoutDrawer.tvCompanyName.visibility = View.INVISIBLE
            Glide.with(this).load(R.drawable.ic_white_profile_place_holder).into(mBinding.layoutDrawer.ivUserpic)
        }
    }

    private fun closeDrawer() {
        mBinding.dlLayout.apply {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            }
        }
    }

    var selectedDrawerPosition = -1
    fun onDrawerItemSelected(position: Int) {
        drawerAdapters.apply {
            if (selectedDrawerPosition != -1) {
                items[selectedDrawerPosition].isChangeable = false
                notifyItemChanged(selectedDrawerPosition)
            } else {
                items[position].isChangeable = true
                notifyItemChanged(position)
                selectedDrawerPosition = position
            }
        }
        when (position) {
            1 -> {
                showToast("Open 1 Fragment")
                closeDrawer()
            }
            2 -> {
                showToast("Open 2Fragment")
                closeDrawer()
            }

            3 -> {
                showToast("Open 3 Fragment")
                closeDrawer()

            }
            4 -> {
                showToast("Open 4 Fragment")
                closeDrawer()
            }

            5 -> {
                showToast("Open 5 Fragment")
                closeDrawer()
            }
            6 -> {
                showToast("Open 5 Fragment")
                closeDrawer()
            }
            7 -> {
                logoutDialog()
                closeDrawer()
            }
            else -> closeDrawer()
        }
    }

}
