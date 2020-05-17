package com.zgame.zgame.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.adapter.CustomerAdapter
import com.zgame.zgame.adapter.CustomerContactAdapter
import com.zgame.zgame.adapter.UserProfileAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.UserProfileContract
import com.zgame.zgame.databinding.FragmentUserProfileBinding
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.UserProfilePresenter
import com.zgame.zgame.utils.Constant
import java.lang.NullPointerException

class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>(),  UserProfileContract.UserProfileView {

    private var currentUserData : SignUpModel? = SignUpModel()

    lateinit var mBinding: FragmentUserProfileBinding
    lateinit var presenter : UserProfilePresenter
    private var uniqueName : String? = null
    lateinit var profileAdapter : UserProfileAdapter

    override fun getContentView(): Int = R.layout.fragment_user_profile

    override fun initNav(view: View) {
    }

    override fun initView(binding: FragmentUserProfileBinding) {
        mBinding = binding
        presenter = UserProfilePresenter(this)
        uniqueName  = PreferanceRepository.getString(Constant.uniqueName)

        initRecyclerView()
        mBinding.txtLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        if(mAuth.currentUser!=null){
            mBinding.txtLogin.visibility = View.GONE
            //mBinding.llUserData.visibility = View.VISIBLE

            setUserAvailableData()
            getUserImages()

        }else{
            mBinding.txtLogin.visibility = View.VISIBLE
            //mBinding.llUserData.visibility = View.GONE
        }
    }

    private fun getUserImages() {
        presenter.getUserImages(uniqueName)
    }

    private fun setUserAvailableData() {
        try{
            currentUserData =   Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
            mBinding.userData = currentUserData
            Glide.with(activity!!).load(currentUserData?.profilePic).into(mBinding.imgProfile)

        }catch (e: NullPointerException){
            showToast(e.message.toString())
        }
    }

    override fun fetchSuccessfully(userGalleryImages: ArrayList<String>) {
        //rv_update
        profileAdapter.addImages(userGalleryImages)
    }

    override fun error(message: String) {
        //if error(show message and random data)
    }

    private fun initRecyclerView(){
        mBinding.rvMyImages.layoutManager = GridLayoutManager(activity, 3)
        profileAdapter = UserProfileAdapter(activity!!)
        mBinding.rvMyImages.adapter = profileAdapter
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setUserImage(PreferanceRepository.getString(Constant.uniqueName), PreferanceRepository.getString(Constant.profilePic))
    }
}