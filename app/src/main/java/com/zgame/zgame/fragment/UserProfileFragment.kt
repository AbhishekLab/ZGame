package com.zgame.zgame.fragment

import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.zgame.zgame.R
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentUserProfileBinding

class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    lateinit var fDatabase : FirebaseDatabase
    lateinit var fRootReference : DatabaseReference
    lateinit var fChildReference: DatabaseReference

    lateinit var mBinding: FragmentUserProfileBinding

    override fun getContentView(): Int = R.layout.fragment_user_profile

    override fun initNav(view: View) {
    }

    override fun initView(binding: FragmentUserProfileBinding) {
        mBinding = binding
        fDatabase = FirebaseDatabase.getInstance()
        fRootReference = fDatabase.reference
        fChildReference = fRootReference.child("message")

        Glide.with(this).load(R.drawable.user_white).into(mBinding.imgProfile)

        mBinding.txtLogin.setOnClickListener { startActivity(Intent(context,LoginActivity::class.java)) }

    }

    override fun onStart() {
        super.onStart()
        fChildReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                d("SendData", "SEnd dara call " +p0.value)

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}