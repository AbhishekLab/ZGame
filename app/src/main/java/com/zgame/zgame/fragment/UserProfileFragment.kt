package com.zgame.zgame.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.CommonSliderActivity
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.adapter.UserProfileAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.UserProfileContract
import com.zgame.zgame.databinding.FragmentUserProfileBinding
import com.zgame.zgame.model.ImageObj
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.UserProfilePresenter
import com.zgame.zgame.utils.Constant
import java.lang.NullPointerException

class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>(),
    UserProfileContract.UserProfileView, UserProfileAdapter.OpenImageListener {

    private var currentUserData: SignUpModel? = SignUpModel()

    lateinit var mBinding: FragmentUserProfileBinding
    lateinit var presenter: UserProfilePresenter
    private var uniqueName: String? = null
    lateinit var profileAdapter: UserProfileAdapter
    private var image: ArrayList<String>? = null

    override fun getContentView(): Int = R.layout.fragment_user_profile

    override fun initNav(view: View) {
    }

    override fun initView(binding: FragmentUserProfileBinding) {
        mBinding = binding

        (activity as MainActivity).showFloatingButton()

        presenter = UserProfilePresenter(this)
        uniqueName = PreferanceRepository.getString(Constant.uniqueName)

        profileAdapter = UserProfileAdapter(activity!!,this)

        mBinding.txtLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        if (mAuth.currentUser != null) {
            mBinding.txtLogin.visibility = View.GONE
            //mBinding.llUserData.visibility = View.VISIBLE

            setUserAvailableData()
            getUserImages()

        } else {
            mBinding.txtLogin.visibility = View.VISIBLE
            //mBinding.llUserData.visibility = View.GONE
        }
    }

    private fun getUserImages() {
        presenter.getUserImages(uniqueName)
    }

    private fun setUserAvailableData() {
        try {
            currentUserData = Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
            mBinding.userData = currentUserData

            if(currentUserData?.profilePic == null){
                Glide.with(activity!!).load(R.drawable.ic_white_profile_place_holder).apply {
                    RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder).circleCrop()
                }.into(mBinding.imgProfile)

            }else{
                Glide.with(activity!!).load(currentUserData?.profilePic).apply {
                    RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder).circleCrop()
                }.into(mBinding.imgProfile)
            }


        } catch (e: NullPointerException) {
            showToast(e.message.toString())
        }
    }

    override fun fetchSuccessfully(userGalleryImages: ImageObj) {

        mBinding.rvMyImages.layoutManager = GridLayoutManager(activity, 3)
        mBinding.rvMyImages.adapter = profileAdapter
        image = ArrayList()

        userGalleryImages.image.map {
            image?.add(it.images)
        }
        if (image!!.size == 0) {
            showToast("No data found")
        } else {  //rv_update
            profileAdapter.addImages(image!!)
        }
    }

    override fun error(message: String) {
        //if error(show message and random data)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setUserImage(
            PreferanceRepository.getString(Constant.uniqueName),
            PreferanceRepository.getString(Constant.profilePic),
            currentUserData?.follower?.size
        )
    }

    override fun openImage(adapterPosition: Int) {
        startActivity(Intent(context, CommonSliderActivity::class.java).putExtra("user_images", image).putExtra("position", adapterPosition))
    }
}