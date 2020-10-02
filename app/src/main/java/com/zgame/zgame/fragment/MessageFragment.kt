package com.zgame.zgame.fragment

import android.content.Intent
import android.view.View
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.chatting.ChattingMainActivity
import com.zgame.zgame.databinding.FragmentMessageSeviceBinding

class MessageFragment : BaseFragment<FragmentMessageSeviceBinding>() {


    override fun initNav(view: View) {
    }

    lateinit var mBinding: FragmentMessageSeviceBinding

    override fun getContentView(): Int = R.layout.fragment_message_sevice

    override fun initView(binding: FragmentMessageSeviceBinding) {
        mBinding = binding
        (activity as MainActivity).hideFloatingButton()


        mBinding.chat.setOnClickListener {
            startActivity(Intent(requireContext(), ChattingMainActivity::class.java))
        }
    }
}