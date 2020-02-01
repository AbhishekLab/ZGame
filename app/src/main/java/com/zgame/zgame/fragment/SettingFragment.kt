package com.zgame.zgame.fragment

import android.view.View
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentSettingBinding

class SettingFragment  : BaseFragment<FragmentSettingBinding>() {
    override fun initNav(view: View) {
    }

    lateinit var mBinding: FragmentSettingBinding
    override fun getContentView(): Int = R.layout.fragment_setting

    override fun initView(binding: FragmentSettingBinding) {
        mBinding = binding

    }
}