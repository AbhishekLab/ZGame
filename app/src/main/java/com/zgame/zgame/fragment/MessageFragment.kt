package com.zgame.zgame.fragment

import android.content.Intent
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.SelectUserActivity
import com.zgame.zgame.adapter.SectionsPagerAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentMessageSeviceBinding
import java.util.*

class MessageFragment : BaseFragment<FragmentMessageSeviceBinding>() {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun initNav(view: View) {
    }

    lateinit var mBinding: FragmentMessageSeviceBinding

    override fun getContentView(): Int = R.layout.fragment_message_sevice

    override fun initView(binding: FragmentMessageSeviceBinding) {
        mBinding = binding
        (activity as MainActivity).hideFloatingButton()

        mSectionsPagerAdapter = SectionsPagerAdapter(activity!!.supportFragmentManager)
        mBinding.viewPage.adapter = mSectionsPagerAdapter

        mBinding.viewPage.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs))
        mBinding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewPage))

        sendRegistrationToServer()

        mBinding.makeRoomBtn.setOnClickListener { v ->
            startActivity(
                Intent(
                    v.context,
                    SelectUserActivity::class.java
                )
            )
        }

    }

    private fun sendRegistrationToServer() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val token: String = FirebaseInstanceId.getInstance().token.toString()
        val map: MutableMap<String, Any> =
            HashMap()
        map["token"] = token
        FirebaseFirestore.getInstance().collection("users").document(uid)[map] = SetOptions.merge()
    }
}