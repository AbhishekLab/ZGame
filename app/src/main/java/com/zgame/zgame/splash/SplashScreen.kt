package com.zgame.zgame.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        Glide.with(this).load(R.drawable.splash_screen).into(img_splash_girl)

        Thread{
            for (i in 0 until 1){
                Thread.sleep(1000)
            }
            this.startActivity(Intent(this,MainActivity::class.java))
        }.start()
    }
}