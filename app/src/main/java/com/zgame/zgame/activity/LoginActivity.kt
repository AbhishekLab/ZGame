package com.zgame.zgame.activity

import android.content.Intent
import android.util.Log.d
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityLoginBinding
import com.zgame.zgame.utils.Validation


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    lateinit var mBinding: ActivityLoginBinding

    override fun contentView(): Int = R.layout.activity_login

    override fun initUI(binding: ActivityLoginBinding) {
        mBinding = binding

        mBinding.toolBar.imgBack.setOnClickListener { finish() }
        mBinding.toolBar.txtTitle.text = resources.getString(R.string.login_dashboard_title)
        mBinding.txtForgotPassword.setOnClickListener { forgotPassword() }
        mBinding.txtSignUp.setOnClickListener { userSignUp() }
        mBinding.txtLogin.setOnClickListener {
            userLogin(
                mBinding.edtEmail.text.toString(),
                mBinding.edtPassword.text.toString()
            )
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        if (mAuth.currentUser != null) {
            d("Yes", "yes User")
        } else {
            d("No", "No User")
        }
    }

    private fun userLogin(email: String, password: String) {
        if (Validation.emailValidation(email, password)) {

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        if(mAuth.currentUser?.isEmailVerified!!){
                            showToast("Login done")
                        }else{
                            showToast("Please Verify your account before use")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this, "Login failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } else {
            showToast("Please Enter valid EmailId")
        }
    }

    private fun userSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun forgotPassword() {

    }
}