package com.zgame.zgame.activity

import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivitySignUpBinding
import com.zgame.zgame.model.UserRequest
import com.zgame.zgame.utils.Validation

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private var userImages : ArrayList<String> ? = null

    override fun onPermissionsGranted(requestCode: Int) {

    }

    private lateinit var mBinding: ActivitySignUpBinding

    override fun contentView(): Int = R.layout.activity_sign_up
    private var name: String = ""

    override fun initUI(binding: ActivitySignUpBinding) {
        mBinding = binding
        mBinding.toolBar.txtTitle.text = resources.getString(R.string.sign_up)
        mBinding.toolBar.imgBack.setOnClickListener { finish() }

        mBinding.txtSignUp.setOnClickListener {
            if (signUpValidation(
                    mBinding.edtFirstName.text.trim().toString(),
                    mBinding.edtLastName.text.trim().toString(),
                    mBinding.edtEmail.text.trim().toString(),
                    mBinding.edtPassword.text.trim().toString(),
                    mBinding.edtConfirmPassword.text.trim().toString(),
                    mBinding.edtMobileNumber.text.trim().toString()
                )
            ) {

                if (Validation.emailValidation(
                        mBinding.edtEmail.text.trim().toString(),
                        mBinding.edtPassword.text.trim().toString()
                    )
                ) {
                    name = "${mBinding.edtFirstName.text} ${mBinding.edtLastName.text}"

                    userSignUp()

                } else {
                    mBinding.edtEmail.error = "Enter valid email address"
                }

            } else {
                showToast("Validation not success")
            }
        }
    }

    private fun userSignUp() {
        mBinding.progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(
            mBinding.edtEmail.text.trim().toString(),
            mBinding.edtPassword.text.trim().toString()
        )
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            val userData = UserRequest(
                                mBinding.edtFirstName.text.trim().toString(),
                                mBinding.edtLastName.text.trim().toString(),
                                mBinding.edtEmail.text.trim().toString(),
                                mBinding.edtPassword.text.trim().toString(),
                                mBinding.edtMobileNumber.text.trim().toString()
                            )
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(mAuth.currentUser!!.uid).child(name)
                                .setValue(userData).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        showToast("Register successfully. Please check your email for verification")
                                        mBinding.progressBar.visibility = View.GONE
                                        finish()
                                    } else {
                                        showToast(resources.getString(R.string.something_went_wrong))
                                        mBinding.progressBar.visibility = View.GONE
                                    }
                                }
                        } else {
                            mBinding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Register not done. Email Id use in different account",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    mBinding.progressBar.visibility = View.GONE
                    showToast(resources.getString(R.string.something_went_wrong))
                }
            }
    }

    private fun signUpValidation(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        mobileNumber: String
    ): Boolean {

        if (firstName.isBlank()) {
            mBinding.edtFirstName.error = "Enter First Name"
            return false
        }
        if (lastName.isBlank()) {
            mBinding.edtLastName.error = "Enter Last Name"
            return false
        }
        if (email.isBlank()) {
            mBinding.edtEmail.error = "Enter Valid Email Address"
            return false
        }
        if (password.isBlank()) {
            mBinding.edtPassword.error = "Enter password"
            return false
        }
        if (confirmPassword.isBlank()) {
            mBinding.edtConfirmPassword.error = "Password does't match"
            return false
        }
        if (password.length < 6) {
            mBinding.edtPassword.error = "Password length should be greater than 6"
            return false
        }
        if (mobileNumber.isBlank()) {
            mBinding.edtMobileNumber.error = "Enter Mobile Number"
            return false
        }
        if (mobileNumber.length != 10) {
            mBinding.edtMobileNumber.error = "10 digit mobile Number"
            return false
        }
        return true
    }
}