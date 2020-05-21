package com.zgame.zgame.activity

import android.util.Log.e
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityEditProfileBinding


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {

    private lateinit var mBinding: ActivityEditProfileBinding

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView(): Int  = R.layout.activity_edit_profile


    override fun initUI(binding: ActivityEditProfileBinding) {
        mBinding = binding

        mBinding.toolbar.txtTitle.text = resources.getString(R.string.update_profile)

    }
}