package com.zgame.zgame.fragment

import android.content.Intent
import android.util.Log.d
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.zgame.zgame.R
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentUserProfileBinding
import com.zgame.zgame.model.UserResponse
import com.zgame.zgame.utils.Constant

class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    private var fRootReference: DatabaseReference? =  null

    lateinit var mBinding: FragmentUserProfileBinding

    override fun getContentView(): Int = R.layout.fragment_user_profile

    override fun initNav(view: View) {
    }

    override fun initView(binding: FragmentUserProfileBinding) {
        mBinding = binding

        Glide.with(this).load(R.drawable.user_white).into(mBinding.imgProfile)
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.txtLogin.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    LoginActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            mBinding.txtLogin.visibility = View.GONE
            mBinding.llUserData.visibility = View.VISIBLE
            fRootReference = FirebaseDatabase.getInstance().getReference(Constant.DbName).child(mAuth.currentUser!!.uid)
        } else {
            mBinding.txtLogin.visibility = View.VISIBLE
            mBinding.llUserData.visibility = View.GONE
        }

        fRootReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for(userData : DataSnapshot in p0.children){
                    val response: UserResponse = userData.getValue(UserResponse::class.java)!!
                    mBinding.txtUserName.text = response.firstName + response.lastName
                }
                mBinding.progressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) {
            showToast(resources.getString(R.string.something_went_wrong))
                mBinding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()

        d("Yes", "yes OnResume")

    }
}