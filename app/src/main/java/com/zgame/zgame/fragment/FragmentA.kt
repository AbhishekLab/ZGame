package com.zgame.zgame.fragment

import android.view.View
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentABinding

class FragmentA : BaseFragment<FragmentABinding>() {


    lateinit var mBinding: FragmentABinding

    override fun getContentView(): Int = R.layout.fragment_a

    override fun initView(binding: FragmentABinding) {
        mBinding = binding

    }
    override fun initNav(view: View) {

    }
}




