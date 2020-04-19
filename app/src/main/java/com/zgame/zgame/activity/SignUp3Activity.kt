package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.zgame.zgame.R
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.contract.SignUpContract
import com.zgame.zgame.databinding.ActivitySignUp3Binding
import com.zgame.zgame.model.AgeModule
import com.zgame.zgame.model.GenderModule
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.SignUpPresenter
import com.zgame.zgame.utils.Constant
import java.io.ByteArrayOutputStream


class SignUp3Activity : BaseActivity<ActivitySignUp3Binding>(), SignUpContract.SignUpView {

    private var profilePhoto: Uri? = null
    private var selectedAge: String? = null
    private var selectedHeight: String? = null
    private var selectedCountry: String? = null
    private var selectedState: String? = null

    private var ageSelectedValue: ArrayList<AgeModule>? = ArrayList()
    private var genderSelectedValue: ArrayList<GenderModule>? = ArrayList()

    private var name: String? = null
    private var email: String? = null
    private var userName: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null

    private var male: String? = null
    private var female: String? = null
    private var coupleFM: String? = null
    private var coupleFF: String? = null
    private var coupleMM: String? = null

    private var isNameChecked : Boolean = false

    private var alertDialog: AlertDialog? = null

    private lateinit var signUpModel: SignUpModel
    private lateinit var presenter: SignUpPresenter

    lateinit var mBinding: ActivitySignUp3Binding

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            Constant.CAMERA_PERMISSION -> takeCamera()
        }
    }

    override fun contentView() = R.layout.activity_sign_up3

    override fun initUI(binding: ActivitySignUp3Binding) {
        mBinding = binding
        presenter = SignUpPresenter(this)

        signUpModel = SignUpModel()

        genderSelectedValue = intent.getParcelableArrayListExtra("I_Am")
        male = intent.getStringExtra(Constant.male)
        female = intent.getStringExtra(Constant.female)
        coupleFF = intent.getStringExtra(Constant.coupleFF)
        coupleFM = intent.getStringExtra(Constant.coupleFM)
        coupleMM = intent.getStringExtra(Constant.coupleMM)
        ageSelectedValue = intent.getParcelableArrayListExtra("Age_Range")
        selectedAge = intent.getStringExtra("Age")
        selectedHeight = intent.getStringExtra("Height")
        selectedCountry = intent.getStringExtra("Country")
        selectedState = intent.getStringExtra("State")

        mBinding.toolbar.imgBack.setOnClickListener { finish() }

        mBinding.txtSearchName.setOnClickListener {
            when {
                mBinding.etUserName.text.isNullOrEmpty() -> {
                    mBinding.etUserName.error = "Enter Unique User Name"
                }
                mBinding.etUserName?.text!!.length>4 -> {
                    isNameChecked = true
                    mBinding.nameProgress.visibility = View.VISIBLE
                    presenter.checkUserName(mBinding.etUserName.text?.trim().toString())
                }
                else -> {
                    showToast("Unique Name should greater than 4 letter")
                }
            }
        }

        mBinding.imgCheckName.setOnClickListener {
            if (mBinding.imgCheckName.drawable.constantState!! == resources.getDrawable(R.drawable.ic_close_brown).constantState) {
                mBinding.etUserName.setText("")
                mBinding.imgCheckName.visibility = View.GONE
            }
        }

        mBinding.userImage.setOnClickListener {
            uploadImage()
        }

        mBinding.btnSubmit.setOnClickListener {
            name = mBinding.etName.text.toString()
            email = mBinding.etUserEmail.text.toString()
            userName = mBinding.etUserName.text.toString()
            password = mBinding.etUserPassword.text.toString()
            confirmPassword = mBinding.etConfirmPassword.text.toString()

            if (signUpValidation(name!!, userName!!, email!!, password!!, confirmPassword!!)) {

                for (i in genderSelectedValue?.indices!!) {
                    signUpModel.gender?.add(genderSelectedValue!![i].name!!)
                }

                for (i in ageSelectedValue?.indices!!) {
                    signUpModel.ageRange?.add(ageSelectedValue!![i].name!!)
                }

                signUpModel.male = male
                signUpModel.female = female
                signUpModel.coupleFF = coupleFF
                signUpModel.coupleFM = coupleFM
                signUpModel.coupleMM = coupleMM
                signUpModel.age = selectedAge
                signUpModel.height = selectedHeight
                signUpModel.country = selectedCountry
                signUpModel.state = selectedState
                signUpModel.email = email
                signUpModel.userName = userName
                signUpModel.password = password

                if (presenter.emailValidation(email!!)) {
                    if(isNameChecked && mBinding.imgCheckName.drawable!!.constantState != resources.getDrawable(R.drawable.ic_close_brown).constantState){
                        mBinding.progressBar.visibility = View.VISIBLE
                        presenter.doSignUp(signUpModel,profilePhoto)
                    }else{
                       showToast("Please Choose Name")
                    }
                } else {
                    showToast("Please Enter Valid Email ")
                }
            }
        }


        mBinding.etUserName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length == 0){
                    mBinding.imgCheckName.visibility = View.GONE
                }
            }
        })
    }

    private fun uploadImage() {
            val dialogBuilder = AlertDialog.Builder(this)
            val layoutView: View = layoutInflater.inflate(R.layout.dialog_file_upload, null)
            val fromFacebook: TextView = layoutView.findViewById(R.id.txt_choose_from_fb)
            val takeCamera: TextView = layoutView.findViewById(R.id.txt_take_pic)
            val fromExisting: TextView = layoutView.findViewById(R.id.txt_choose_existing)
            dialogBuilder.setView(layoutView)
            alertDialog = dialogBuilder.create()
            alertDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()


        takeCamera.setOnClickListener {
            requestAppPermissions(
                arrayOf(Manifest.permission.CAMERA), R.string.permission_text, Constant.CAMERA_PERMISSION
            )
        }
    }


    private fun takeCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Constant.CAMERA_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constant.CAMERA_PERMISSION && resultCode == Activity.RESULT_OK){
            val imgInBitmapDrawable =  data!!.extras!!["data"] as Bitmap?

           // mBinding.userImage.setImageBitmap(profilePhoto)

            val bytes = ByteArrayOutputStream()
            imgInBitmapDrawable?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(contentResolver, imgInBitmapDrawable, "ProfilePic", null)
            profilePhoto = Uri.parse(path.toString())
            Glide.with(this).load(profilePhoto).into(mBinding.userImage)

        }
    }

    private fun signUpValidation(
        name: String,
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        if (name.isBlank()) {
            mBinding.etName.error = "Enter Name"
            return false
        }
        if(name.length<4){
            mBinding.etName.error = "Name should greater than 4 letter"
            return false
        }
        if (userName.isBlank()) {
            mBinding.etUserName.error = "Enter Unique Name"
            return false
        }
        if(userName.length<4){
            mBinding.etUserName.error = "Unique Name should greater than 4 letter"
            return false
        }
        if (email.isBlank()) {
            mBinding.etUserEmail.error = "Enter Email Address"
            return false
        }
        if (password.isBlank()) {
            mBinding.etUserPassword.error = "Enter password"
            return false
        }
        if (confirmPassword.isBlank()) {
            mBinding.etConfirmPassword.error = "Password does't match"
            return false
        }
        if (password.length < 6) {
            mBinding.etUserPassword.error = "Password length should be greater than 6"
            return false
        }
        if (password != confirmPassword) {
            mBinding.etConfirmPassword.error = "Password mismatch"
            return false
        }
        return true
    }

    override fun registerDone() {
        showToast(resources.getString(R.string.register_done))
        mBinding.progressBar.visibility = View.GONE
    }

    override fun registerNotDone(message: String?) {
        mBinding.progressBar.visibility = View.GONE
        showToast(message!!)
    }

    override fun uniqueNameCorrect() {
        mBinding.nameProgress.visibility = View.GONE
        mBinding.imgCheckName.visibility = View.VISIBLE
        mBinding.imgCheckName.setImageResource(R.drawable.ic_close_brown)
    }

    override fun uniqueNameInCorrect() {
        mBinding.nameProgress.visibility = View.GONE
        mBinding.imgCheckName.visibility = View.VISIBLE
        mBinding.imgCheckName.setImageResource(R.drawable.ic_check)

    }

    override fun databaseError(message: String) {
        showToast(message)
        mBinding.nameProgress.visibility = View.GONE

    }
}