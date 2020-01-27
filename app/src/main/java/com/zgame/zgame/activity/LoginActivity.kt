package com.zgame.zgame.activity

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
    lateinit var mAuth: FirebaseAuth

    override fun contentView(): Int = R.layout.activity_login

    override fun initUI(binding: ActivityLoginBinding) {
        mBinding = binding

        mAuth = FirebaseAuth.getInstance()

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

        if(mAuth.currentUser !=null){
            d("Yes","yes User")
        }else{
            d("No","No User")
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        //d("CurrentUser", currentUser!!.displayName)
    }

    private fun userLogin(email: String, password: String) {
        if (Validation.loginValidation(email, password)) {

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        d("sucess", "signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        d("sucess", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } else {
            showToast("Please Enter valid EmailId")
        }
    }

    private fun userSignUp() {

    }

    private fun forgotPassword() {

    }
}