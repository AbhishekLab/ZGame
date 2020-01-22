package com.zgame.zgame.splash

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivitySplashBinding

class SplashScreen : BaseActivity<ActivitySplashBinding>() {

    lateinit var mBinding:ActivitySplashBinding

    override fun contentView(): Int = R.layout.activity_splash

    override fun initUI(binding: ActivitySplashBinding) {
        mBinding = binding
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        mBinding.progressBar.progress = 0

        Glide.with(this).load(R.raw.splash_girl).into(mBinding.imgSplashGirl)

        Thread{
            for (i in 0 until 11){
                mBinding.progressBar.progress = i
                Thread.sleep(1000)
            }
            this.startActivity(Intent(this,MainActivity::class.java))
        }.start()


    }
}