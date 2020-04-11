package com.zgame.zgame.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.databinding.ActivityCustomerDetailBinding
import com.zgame.zgame.model.CustomerData
import com.zgame.zgame.presenter.CustomerDetailPresenter

class CustomerDetailActivity : BaseActivity<ActivityCustomerDetailBinding>(),
    CustomerDetailContract.CustomerDetailView {

    private lateinit var mBinding: ActivityCustomerDetailBinding

    private var customerDetail: ArrayList<CustomerData>? = null
    private var customerDetailResponse: CustomerData? = null
    private var nameId: String? = ""
    private var alertDialog: AlertDialog? = null
    private var progressBar : ProgressBar? = null
    private lateinit var presenter: CustomerDetailPresenter

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_customer_detail

    override fun initUI(binding: ActivityCustomerDetailBinding) {
        mBinding = binding

        presenter = CustomerDetailPresenter(this@CustomerDetailActivity)

        mBinding.toolbar.apply { setUpToolbar()}
        mBinding.llWink.setOnClickListener { presenter.wink()}

        if (mAuth.currentUser == null) {
            setDialog()
        }
        nameId = intent?.getStringExtra("id")
        presenter.customerDetail(nameId!!)

    }

    override fun getCustomerDetail(p0: DataSnapshot) {
        customerDetail = ArrayList()
        for (userData: DataSnapshot in p0.children.iterator()) {
            customerDetailResponse = userData.getValue(CustomerData::class.java)
        }
        Glide.with(this@CustomerDetailActivity).load(customerDetailResponse?.image)
            .into(mBinding.imgUser)
    }

    override fun loginSuccess() {
        showToast("Welcome User")
        progressBar?.visibility = View.GONE
        alertDialog?.dismiss()
    }

    override fun loginFailed(message: String) {
        showToast(message)
        progressBar?.visibility = View.GONE
    }

    private fun setUpToolbar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.title = ""
        mBinding.ivBack.setOnClickListener { finish() }
    }

    private fun setDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val layoutView: View = layoutInflater.inflate(R.layout.dialog_negative_layout, null)
        val dialogButton: Button = layoutView.findViewById(R.id.btnDialog)
        val etLogin: EditText = layoutView.findViewById(R.id.et_login)
        val etPassword: EditText = layoutView.findViewById(R.id.et_password)
        val close: ImageView = layoutView.findViewById(R.id.img_close)
        progressBar = layoutView.findViewById(R.id.progress_bar)
        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()
        alertDialog?.setCancelable(false)
        dialogButton.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            presenter.dialogLogin(etLogin.text.toString(), etPassword.text.toString())
        }
        close.setOnClickListener { finish() }
        etLogin.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) etLogin.hint = "" else etLogin.hint = "Email Id"
        }
        etPassword.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) etPassword.hint = "" else etPassword.hint = "Password"
        }

    }
    override fun winkAdded() {

    }

}