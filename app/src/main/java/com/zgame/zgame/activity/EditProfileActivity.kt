package com.zgame.zgame.activity

import android.util.Log.e
import android.view.View
import com.anupcowkur.reservoir.Reservoir
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.UpdateProfileContract
import com.zgame.zgame.databinding.ActivityEditProfileBinding
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.model.UpdateProfileModel
import com.zgame.zgame.presenter.UpdateProfilePresenter
import com.zgame.zgame.utils.Constant


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(),
    UpdateProfileContract.UpdateProfileView {

    private lateinit var mBinding: ActivityEditProfileBinding
    private lateinit var presenter: UpdateProfilePresenter
    private var bodyHairChips: ArrayList<String> = ArrayList()
    private var childBodyHairChipsText: StringBuilder? = StringBuilder()

    private var mySexyualComfortChips: ArrayList<String> = ArrayList()
    private var sexualComfortChipsText: StringBuilder? = StringBuilder()

    private var whatDoYouLikeChips: ArrayList<String> = ArrayList()
    private var yourLikeChipsText: StringBuilder? = StringBuilder()

    private var updateModel: UpdateProfileModel? = null
    private var uniqueName: String = ""
    private var loginRespponse: SignUpModel = SignUpModel()

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView(): Int = R.layout.activity_edit_profile


    override fun initUI(binding: ActivityEditProfileBinding) {
        mBinding = binding


        uniqueName = PreferanceRepository.getString(Constant.uniqueName)
        presenter = UpdateProfilePresenter(this, uniqueName)

        mBinding.toolbar.txtTitle.text = resources.getString(R.string.update_profile)
        mBinding.txtMarquee.isSelected = true
        mBinding.toolbar.imgBack.setOnClickListener { finish() }

        setPreData()
        setData()



        mBinding.btnUpdate.setOnClickListener {
            mBinding.progressBar.visibility = View.VISIBLE
            presenter.updateProfile(getAllValue())
        }


        for (index in 0 until mBinding.cgBodyHair.childCount) {
            val chip: Chip = mBinding.cgBodyHair.getChildAt(index) as Chip

            // Set the chip checked change listener
            chip.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    bodyHairChips.add(view.text.toString())
                } else {
                    bodyHairChips.remove(view.text.toString())
                }
            }
        }

        for (index in 0 until mBinding.cgMmySexualComfort.childCount) {
            val chip: Chip = mBinding.cgMmySexualComfort.getChildAt(index) as Chip

            // Set the chip checked change listener
            chip.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    mySexyualComfortChips.add(view.text.toString())
                } else {
                    mySexyualComfortChips.remove(view.text.toString())
                }
            }
        }

        for (index in 0 until mBinding.cgWhatDoYouLike.childCount) {
            val chip: Chip = mBinding.cgWhatDoYouLike.getChildAt(index) as Chip

            // Set the chip checked change listener
            chip.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    whatDoYouLikeChips.add(view.text.toString())
                } else {
                    whatDoYouLikeChips.remove(view.text.toString())
                }
            }
        }
    }

    private fun setPreData() {
        try {
            loginRespponse = Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
            mBinding.userData = loginRespponse

            if (loginRespponse.profilePic == null) {
                Glide.with(this).load(R.drawable.ic_white_profile_place_holder).apply {
                    RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder)
                        .circleCrop()
                }.into(mBinding.imgProfile)

            } else {
                Glide.with(this).load(loginRespponse.profilePic).apply {
                    RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder)
                        .circleCrop()
                }.into(mBinding.imgProfile)
            }
        } catch (e: NullPointerException) {
            showToast(e.message.toString())
        }
    }


    private fun getAllValue(): UpdateProfileModel {
        updateModel = UpdateProfileModel()
        childBodyHairChipsText?.clear()

        if (mBinding.spWeight.selectedItem.toString() != "Select Weight") {
            updateModel?.weight = mBinding.spWeight.selectedItem.toString()
        }
        if (mBinding.spBodyType.selectedItem.toString() != "Select Body Type") {
            updateModel?.bodyType = mBinding.spBodyType.selectedItem.toString()
        }
        if (mBinding.spEthnicBackground.selectedItem.toString() != "Select Ethnic background") {
            updateModel?.ethnicBackground = mBinding.spEthnicBackground.selectedItem.toString()
        }
        updateModel?.smokes = mBinding.spSmoke.selectedItem.toString()

        updateModel?.tattoos = mBinding.spTattoo.selectedItem.toString()

        if (mBinding.spSexuality.selectedItem.toString() != "Select Sexuality") {
            updateModel?.sexuality = mBinding.spSexuality.selectedItem.toString()
        }
        updateModel?.exprience = mBinding.spExperience.selectedItem.toString()

        updateModel?.looksAreImportant = mBinding.spLooksAreImportant.selectedItem.toString()

        if (bodyHairChips != null && bodyHairChips.isNotEmpty()) {
            bodyHairChips.forEachIndexed { pos, it ->

                childBodyHairChipsText?.append(it)

                if (pos != (bodyHairChips.size - 1)) {
                    childBodyHairChipsText?.append(",")
                }
            }
        }

        if (childBodyHairChipsText!!.isNotEmpty()) {
            updateModel?.bodyHair = childBodyHairChipsText.toString()
        }


        if (mySexyualComfortChips != null && mySexyualComfortChips.isNotEmpty()) {
            mySexyualComfortChips.forEachIndexed { pos, it ->
                sexualComfortChipsText?.append(it)
                if (pos != (mySexyualComfortChips.size - 1)) {
                    sexualComfortChipsText?.append(",")
                }
            }
        }

        if (sexualComfortChipsText!!.isNotEmpty()) {
            updateModel?.mySexualComfort = sexualComfortChipsText.toString()
        }

        if (whatDoYouLikeChips != null && whatDoYouLikeChips.isNotEmpty()) {
            whatDoYouLikeChips.forEachIndexed { pos, it ->
                yourLikeChipsText?.append(it)
                if (pos != (whatDoYouLikeChips.size - 1)) {
                    yourLikeChipsText?.append(",")
                }
            }
        }

        if (yourLikeChipsText!!.isNotEmpty()) {
            updateModel?.whatDoYouLike = yourLikeChipsText.toString()
        }

        updateModel?.introduceYourSelf = mBinding.etIntroductionYourSelf.text.toString()
        updateModel?.description = mBinding.dtDescription.text.toString()
        updateModel?.whatYouAreLookingFor = mBinding.etWhatAreYouLookingFor.text.toString()

        return updateModel!!

    }

    override fun error(message: String) {
        showToast("Failed to update your profile!! Please try another time")
        mBinding.progressBar.visibility = View.GONE
    }

    override fun updateSuccessfully(update: String) {
        showToast(update)
        mBinding.progressBar.visibility = View.GONE
    }

    override fun setUserProfileData(response: UpdateProfileModel) {
        val weightAdapter = mBinding.spWeight.adapter
        val bodyTypeAdapter = mBinding.spBodyType.adapter
        val ethnicBackAdapter = mBinding.spEthnicBackground.adapter
        val tattooAdapter = mBinding.spTattoo.adapter
        val sexualityAdapter = mBinding.spSexuality.adapter
        val experienceAdapter = mBinding.spExperience.adapter
        val looksAdapter = mBinding.spLooksAreImportant.adapter

       /* var bodyHair = ArrayList<String>()
        var mySexualComfort = ArrayList<String>()
        var whatDoYpuLike = ArrayList<String>()

        if(response.bodyHair!!.contains(",")){
            bodyHair = response.bodyHair?.split(",") as ArrayList<String>
        }
        if(response.mySexualComfort!!.contains(",")){
            mySexualComfort = response.mySexualComfort?.split(",") as ArrayList<String>
        }
        if(response.mySexualComfort!!.contains(",")){
            whatDoYpuLike = response.whatDoYouLike?.split(",") as ArrayList<String>
        }
*/
        for (position in 0 until weightAdapter.count) {
            if (weightAdapter.getItem(position) == response.weight) {
                mBinding.spWeight.setSelection(position)
            }
        }
        for (position in 0 until bodyTypeAdapter.count) {
            if (bodyTypeAdapter.getItem(position) == response.bodyType) {
                mBinding.spBodyType.setSelection(position)
            }
        }
        for (position in 0 until ethnicBackAdapter.count) {
            if (ethnicBackAdapter.getItem(position) == response.ethnicBackground) {
                mBinding.spEthnicBackground.setSelection(position)
            }
        }
        for (position in 0 until tattooAdapter.count) {
            if (tattooAdapter.getItem(position) == response.tattoos) {
                mBinding.spTattoo.setSelection(position)
            }
        }
        for (position in 0 until sexualityAdapter.count) {
            if (sexualityAdapter.getItem(position) == response.sexuality) {
                mBinding.spSexuality.setSelection(position)
            }
        }
        for (position in 0 until experienceAdapter.count) {
            if (experienceAdapter.getItem(position) == response.exprience) {
                mBinding.spExperience.setSelection(position)
            }
        }
        for (position in 0 until looksAdapter.count) {
            if (looksAdapter.getItem(position) == response.looksAreImportant) {
                mBinding.spLooksAreImportant.setSelection(position)
            }
        }


        for (index in 0 until mBinding.cgBodyHair.childCount) {
            val chip: Chip = mBinding.cgBodyHair.getChildAt(index) as Chip

        }

        for (index in 0 until mBinding.cgMmySexualComfort.childCount) {
            val chip: Chip = mBinding.cgMmySexualComfort.getChildAt(index) as Chip

        }
    }


    private fun setData() {
        presenter.getUserProfileData()
    }
}