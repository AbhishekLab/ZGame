package com.zgame.zgame.activity

import android.content.Intent
import android.util.Log.d
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.contract.LoginContract
import com.zgame.zgame.databinding.ActivityLoginBinding
import com.zgame.zgame.presenter.LoginPresenter
import com.zgame.zgame.utils.Validation


class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginContract.LoginView {

    private var presenter : LoginPresenter? = null

    override fun onPermissionsGranted(requestCode: Int) {
    }

    lateinit var mBinding: ActivityLoginBinding

    override fun contentView(): Int = R.layout.activity_login

    override fun initUI(binding: ActivityLoginBinding) {
        mBinding = binding
        mBinding.toolBar.txtTitle.text = resources.getString(R.string.login_dashboard_title)
        presenter = LoginPresenter(this)


        mBinding.toolBar.imgBack.setOnClickListener { finish() }
        mBinding.txtForgotPassword.setOnClickListener { forgotPassword() }
        mBinding.txtSignUp.setOnClickListener { userSignUp() }
        mBinding.txtLogin.setOnClickListener {
            mBinding.progressBar.visibility = View.VISIBLE
            presenter?.dialogLogin( mBinding.edtEmail.text.toString(),mBinding.edtPassword.text.toString())
        }
    }

    private fun userSignUp() {
        startActivity(Intent(this, SignUp1Activity::class.java))
    }

    private fun forgotPassword() {

    }

    override fun loginSuccess() {
        mBinding.progressBar.visibility = View.GONE
        showToast("Login Success ")
    }

    override fun loginFailed(message: String) {
        mBinding.progressBar.visibility = View.GONE
        showToast(message)
    }
}