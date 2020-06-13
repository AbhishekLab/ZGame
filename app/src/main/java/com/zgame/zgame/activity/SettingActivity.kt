package com.zgame.zgame.activity

import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivitySettingBinding
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant
import java.lang.NullPointerException

class SettingActivity : BaseActivity<ActivitySettingBinding>(){

    private lateinit var mBinding: ActivitySettingBinding
    private var currentUserData : SignUpModel? = null

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView(): Int  = R.layout.activity_setting


    override fun initUI(binding: ActivitySettingBinding) {
        mBinding = binding

        try{
            currentUserData =   Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
        }catch (e: NullPointerException){
            Glide.with(this).load(R.drawable.gradient_3_image).into(mBinding.userImage)
        }

        currentUserData.let {
            apply {
                Glide.with(this).load(it?.profilePic).into(mBinding.userImage)
                Glide.with(this).load(it?.profilePic).into(mBinding.imgBackground)
            }
        }

    }

}