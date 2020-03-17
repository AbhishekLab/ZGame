package com.zgame.zgame.activity

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.zgame.zgame.R
import com.zgame.zgame.adapter.DemoAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityCustomerDetailBinding
import com.zgame.zgame.model.CustomerData

class CustomerDetailActivity : BaseActivity<ActivityCustomerDetailBinding>() {

    private lateinit var mBinding: ActivityCustomerDetailBinding

    private var databaseRef: DatabaseReference? = null
    private var customerDetail: ArrayList<CustomerData>? = null
    private var customerDetailResponse: CustomerData? = null
    private var ref: Query? = null
    private var position: Int = 0


    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_customer_detail

    override fun initUI(binding: ActivityCustomerDetailBinding) {
        mBinding = binding

        mBinding.toolbar.apply { setUpToolbar()}
        setupRecyclerView()

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
}