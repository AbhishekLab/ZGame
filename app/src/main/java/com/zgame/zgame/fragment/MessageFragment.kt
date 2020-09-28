package com.zgame.zgame.fragment

import android.view.View
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.adapter.UserMessageAdapter
import com.zgame.zgame.adapter.UsesrChatsAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentSettingBinding

class MessageFragment : BaseFragment<FragmentSettingBinding>() {

    lateinit var userMessageAdapter : UserMessageAdapter

    override fun initNav(view: View) {
    }

    lateinit var mBinding: FragmentSettingBinding

    override fun getContentView(): Int = R.layout.fragment_setting

    override fun initView(binding: FragmentSettingBinding) {
        mBinding = binding
        (activity as MainActivity).hideFloatingButton()

        userMessageAdapter  = UserMessageAdapter()
        mBinding.rvMessages.adapter = userMessageAdapter
    }
}