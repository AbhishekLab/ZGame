package com.zgame.zgame.activity

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.contract.SignUpContract
import com.zgame.zgame.databinding.ActivitySignUp3Binding
import com.zgame.zgame.model.AgeModule
import com.zgame.zgame.model.GenderModule
import com.zgame.zgame.model.SeekingModule
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.SignUpPresenter

class SignUp3Activity : BaseActivity<ActivitySignUp3Binding>(), SignUpContract.SignUpView {

    private var selectedAge: String? = null
    private var selectedHeight: String? = null
    private var selectedCountry: String? = null
    private var selectedState: String? = null

    private var seekingSelectedValue: ArrayList<SeekingModule>? = ArrayList()
    private var ageSelectedValue: ArrayList<AgeModule>? = ArrayList()
    private var genderSelectedValue: ArrayList<GenderModule>? = ArrayList()

    private var name: String? = null
    private var email: String? = null
    private var userName: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null

    private var isNameChecked : Boolean = false

    private lateinit var signUpModel: SignUpModel
    private lateinit var presenter: SignUpPresenter

    lateinit var mBinding: ActivitySignUp3Binding

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_sign_up3

    override fun initUI(binding: ActivitySignUp3Binding) {
        mBinding = binding
        presenter = SignUpPresenter(this)

        signUpModel = SignUpModel()

        genderSelectedValue = intent.getParcelableArrayListExtra("I_Am")
        seekingSelectedValue = intent.getParcelableArrayListExtra("Seeking")
        ageSelectedValue = intent.getParcelableArrayListExtra("Age_Range")
        selectedAge = intent.getStringExtra("Age")
        selectedHeight = intent.getStringExtra("Height")
        selectedCountry = intent.getStringExtra("Country")
        selectedState = intent.getStringExtra("State")

        mBinding.toolbar.imgBack.setOnClickListener { finish() }

        mBinding.txtSearchName.setOnClickListener {
            if (mBinding.etUserName.text.isNullOrEmpty()) {
                mBinding.etUserName.error = "Enter Unique User Name"
            } else {
                isNameChecked = true
                mBinding.nameProgress.visibility = View.VISIBLE
                presenter.checkUserName(mBinding.etUserName.text?.trim().toString())
            }
        }

        mBinding.imgCheckName.setOnClickListener {
            if (mBinding.imgCheckName.drawable.constantState!! == resources.getDrawable(R.drawable.ic_close_brown).constantState) {
                mBinding.etUserName.setText("")
                mBinding.imgCheckName.visibility = View.GONE
            }
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

                for (i in seekingSelectedValue?.indices!!) {
                    signUpModel.seeking?.add(seekingSelectedValue!![i].name!!)
                }

                for (i in ageSelectedValue?.indices!!) {
                    signUpModel.ageRange?.add(ageSelectedValue!![i].name!!)
                }

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
                        presenter.doSignUp(signUpModel)
                    }else{
                       showToast("Please Choose Name")
                    }

                } else {
                    showToast("Please Enter Valid Email ")
                }
            }
        }


        mBinding.etUserName.addTextChangedListener {
            if(it?.length==0){
                mBinding.imgCheckName.visibility = View.GONE
            }
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
        if (userName.isBlank()) {
            mBinding.etUserName.error = "Enter Unique Name"
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