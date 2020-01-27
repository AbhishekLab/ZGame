package com.zgame.zgame.fragment

import android.view.View
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentBBinding

class FragmentB : BaseFragment<FragmentBBinding>() {
    override fun initNav(view: View) {
    }

    lateinit var mBinding: FragmentBBinding

    override fun getContentView(): Int = R.layout.fragment_b

    override fun initView(binding: FragmentBBinding) {
        mBinding = binding
    }

}
