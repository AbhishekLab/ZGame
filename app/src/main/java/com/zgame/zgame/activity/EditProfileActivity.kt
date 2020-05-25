package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
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
import com.zgame.zgame.utils.Constant.PICK_REQUEST
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(),
    UpdateProfileContract.UpdateProfileView {

    private lateinit var mBinding: ActivityEditProfileBinding
    private lateinit var presenter: UpdateProfilePresenter
    private var bodyHairChips: ArrayList<String> = ArrayList()
    private var childBodyHairChipsText: StringBuilder? = StringBuilder()

    private var mySexualComfortChips: ArrayList<String> = ArrayList()
    private var sexualComfortChipsText: StringBuilder? = StringBuilder()

    private var whatDoYouLikeChips: ArrayList<String> = ArrayList()
    private var yourLikeChipsText: StringBuilder? = StringBuilder()

    private var updateModel: UpdateProfileModel? = null
    private var uniqueName: String = ""
    private var loginRespponse: SignUpModel = SignUpModel()

    private var profilePhoto : Uri? = null

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            PICK_REQUEST -> setImageToProfile()
        }
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
            presenter.updateProfile(getAllValue(), profilePhoto)
        }

        mBinding.btnBrowse.setOnClickListener {
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, PICK_REQUEST
            )
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
                    mySexualComfortChips.add(view.text.toString())
                } else {
                    mySexualComfortChips.remove(view.text.toString())
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


        if (mySexualComfortChips != null && mySexualComfortChips.isNotEmpty()) {
            mySexualComfortChips.forEachIndexed { pos, it ->
                sexualComfortChipsText?.append(it)
                if (pos != (mySexualComfortChips.size - 1)) {
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
        showToast(message)
        mBinding.progressBar.visibility = View.GONE
        mBinding.hzProgressBar.visibility = View.GONE
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

        var bodyHair = ArrayList<String>()
        var mySexualComfort = ArrayList<String>()
        var whatDoYpuLike = ArrayList<String>()

        if (response.bodyHair!!.isNotEmpty()) {
            bodyHair = if (response.bodyHair!!.contains(",")) {
                response.bodyHair?.split(",") as ArrayList<String>
            } else {
                arrayListOf(response.bodyHair!!)
            }
        }

        if (response.mySexualComfort!!.isNotEmpty()) {
            mySexualComfort = if (response.mySexualComfort!!.contains(",")) {
                response.mySexualComfort?.split(",") as ArrayList<String>
            } else {
                arrayListOf(response.mySexualComfort!!)
            }
        }

        if (response.whatDoYouLike!!.isNotEmpty()) {
            whatDoYpuLike = if (response.whatDoYouLike!!.contains(",")) {
                response.whatDoYouLike?.split(",") as ArrayList<String>
            } else {
                arrayListOf(response.whatDoYouLike!!)
            }
        }

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
            if (bodyHair.isNotEmpty()) {
                bodyHair.forEach {
                    if (chip.text.toString() == it) {
                        chip.isChecked = true
                    }
                }
            }
        }

        for (index in 0 until mBinding.cgMmySexualComfort.childCount) {
            val chip: Chip = mBinding.cgMmySexualComfort.getChildAt(index) as Chip
            if (mySexualComfort.isNotEmpty()) {
                mySexualComfort.forEach {
                    if (chip.text.toString() == it) {
                        chip.isChecked = true
                    }
                }
            }
        }

        for (index in 0 until mBinding.cgWhatDoYouLike.childCount) {
            val chip: Chip = mBinding.cgWhatDoYouLike.getChildAt(index) as Chip
            if (whatDoYpuLike.isNotEmpty()) {
                whatDoYpuLike.forEach {
                    if (chip.text.toString() == it) {
                        chip.isChecked = true
                    }
                }
            }
        }

        mBinding.etIntroductionYourSelf.setText(response.introduceYourSelf)
        mBinding.dtDescription.setText(response.description)
        mBinding.etWhatAreYouLookingFor.setText(response.whatYouAreLookingFor)

        mBinding.hzProgressBar.visibility = View.GONE
    }


    private fun setData() {
        mBinding.hzProgressBar.visibility = View.VISIBLE
        presenter.getUserProfileData()
    }

    private fun setImageToProfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_REQUEST -> {
                    try {
                       /* val imgInBitmapDrawable =  data!!.extras!!["data"] as Bitmap?

                        val bytes = ByteArrayOutputStream()
                        imgInBitmapDrawable?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        val path = MediaStore.Images.Media.insertImage(contentResolver, imgInBitmapDrawable, "ProfilePic", null)
                        profilePhoto = Uri.parse(path.toString())
                        Glide.with(this).load(profilePhoto).into(mBinding.imgProfile)
*/

                        val uri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        val bytes = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ProfilePic", null)
                        profilePhoto = Uri.parse(path.toString())

                        Glide.with(this).load(profilePhoto).into(mBinding.imgProfile)


                    } catch (e: IOException) {
                        e("Error", e.message.toString())
                    }
                }
            }

        }
    }
}