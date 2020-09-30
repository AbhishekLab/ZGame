package com.zgame.zgame

import android.Manifest
import android.app.AlertDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.zgame.zgame.activity.*
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.databinding.ActivityMainBinding
import com.zgame.zgame.service.BackgroundService
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.reservoir_key
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val tag = "startService"
    private val jobId = 100
    private lateinit var mBinding: ActivityMainBinding
    private var navController: NavController? = null
    private var alertDialog: AlertDialog? = null

    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            Constant.STORAGE_PERMISSION -> callUtils()
        }
    }

    override fun contentView() = R.layout.activity_main


    override fun initUI(binding: ActivityMainBinding) {
        mBinding = binding
        mAuth = FirebaseAuth.getInstance()

        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.incBottom.bsSheet)

        setSupportActionBar(mBinding.bottomAppBar)
        navController = findNavController(this, R.id.nav_host_fragment)
        mBinding.fab.setImageResource(R.drawable.ic_fab_24dp)
        mBinding.fab.setOnClickListener {
            fabClick()
        }

        mBinding.bottomAppBar.setNavigationOnClickListener {

            if (mBottomSheetBehavior?.state != BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }

        mBottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when(newState){
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        mBinding.fab.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mBinding.fab.show()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }

            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }

        })

        mBinding.incBottom.llProfile.setOnClickListener {
            if(mAuth.currentUser!=null){
                startActivity(Intent(this, EditProfileActivity::class.java))
            }else{
                showToast("Please Login first")
            }
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llMailBox.setOnClickListener {
            showToast("Mail Box")
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llCollection.setOnClickListener {
            showToast("Collection")
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llUpgrade.setOnClickListener {
            showToast("Upgrade")
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llAboutUs.setOnClickListener {
            showToast("About Us")
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.llLogOut.setOnClickListener {
            logoutDialog()
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        mBinding.incBottom.txtLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (navController?.currentDestination?.label == "CustomerGalleryFragment" && item.toString() == "Home" || navController?.currentDestination?.label == "UserProfileFragment" &&
            item.toString() == "Profile" || navController?.currentDestination?.label == "MessageFragment" && item.toString() == "Message"
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
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, UploadImageActivity::class.java))
        } else {
            showToast("Please login to enable this feature")
        }
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

    fun setUserImage(userName: String, userImageUrl: String, follower: Int?) {
        if (mAuth.currentUser != null) {
            mBinding.incBottom.llLoginRoot.visibility = View.VISIBLE
            mBinding.incBottom.llLogOutRoot.visibility = View.GONE
            if (userImageUrl != "null" && userImageUrl != "") {
                Glide.with(this)
                    .load(userImageUrl).apply(
                        RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder)
                    ).into(mBinding.incBottom.imgUserPic)
            } else {
                Glide.with(this).load(R.drawable.ic_white_profile_place_holder)
                    .into(mBinding.incBottom.imgUserPic)
            }
            mBinding.incBottom.txtName.text = userName
            if(follower == null){
                mBinding.incBottom.txtFollower.text = "Followers : 0"
            }else{
                mBinding.incBottom.txtFollower.text = "Followers : $follower"
            }
        }else{
            mBinding.incBottom.llLoginRoot.visibility = View.GONE
            mBinding.incBottom.llLogOutRoot.visibility = View.VISIBLE
        }
    }

    fun hideFloatingButton() {
        mBinding.fab.hide()
    }

    fun showFloatingButton() {
        mBinding.fab.show()
    }
}
