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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.google.firebase.database.*
import com.zgame.zgame.adapter.DemoAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityCustomerDetailBinding
import com.zgame.zgame.model.CustomerData
import com.zgame.zgame.utils.Validation


class CustomerDetailActivity : BaseActivity<ActivityCustomerDetailBinding>() {

    private lateinit var mBinding: ActivityCustomerDetailBinding

    private var databaseRef: DatabaseReference? = null
    private var customerDetail: ArrayList<CustomerData>? = null
    private var customerDetailResponse: CustomerData? = null
    private var ref: Query? = null
    private var position: Int = 0
    private var alertDialog: AlertDialog? = null
    private var progressBar : ProgressBar? = null


    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_customer_detail

    override fun initUI(binding: ActivityCustomerDetailBinding) {
        mBinding = binding

        mBinding.toolbar.apply { setUpToolbar()}
        setupRecyclerView()
        if (mAuth.currentUser == null) {
            setDialog()
        }

        position = intent.getIntExtra("position", 0)
        databaseRef = FirebaseDatabase.getInstance().reference.child("Customers")

        databaseRef.let {
            ref = FirebaseDatabase.getInstance().getReference("Customers").orderByChild("id")
                .equalTo(position.toString())
            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    customerDetail = ArrayList()

                    for (userData: DataSnapshot in p0.children.iterator()) {
                        customerDetailResponse = userData.getValue(CustomerData::class.java)
                    }
                    Glide.with(this@CustomerDetailActivity).load(customerDetailResponse?.image).into(mBinding.imgUser)
                }
            }) ?: showToast("No Data Found")
        }
    }

    private fun setupRecyclerView() {
        mBinding.rvDemo.adapter = DemoAdapter()
    }

    private fun setUpToolbar() {
        setSupportActionBar(mBinding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = ""
        mBinding.toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_action_back)
        mBinding.toolbar.setNavigationOnClickListener { finish() }
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
            doLogin(etLogin.text.toString(), etPassword.text.toString())
        }
        close.setOnClickListener { finish() }
        etLogin.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) etLogin.hint = "" else etLogin.hint = "Email Id"
        }
        etPassword.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) etPassword.hint = "" else etPassword.hint = "Password"
        }

    }

    private fun doLogin(email: String, password: String) {
        progressBar?.visibility = View.VISIBLE
        if (Validation.emailValidation(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser != null) {
                            showToast("Welcome User")
                            progressBar?.visibility = View.GONE
                            alertDialog?.dismiss()
                        } else {
                            showToast("Something went wrong")
                            progressBar?.visibility = View.GONE
                        }
                    } else {
                        progressBar?.visibility = View.GONE
                        showToast("Please check your credential")
                    }
                }

        } else {
            showToast("Please Enter valid EmailId")
            progressBar?.visibility = View.GONE
        }
    }
}