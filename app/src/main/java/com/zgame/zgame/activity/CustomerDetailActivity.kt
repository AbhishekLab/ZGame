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
import com.zgame.zgame.adapter.FeedAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.databinding.ActivityCustomerDetailBinding
import com.zgame.zgame.model.ContactRandomData
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.CustomerDetailPresenter
import com.zgame.zgame.utils.Constant

class CustomerDetailActivity : BaseActivity<ActivityCustomerDetailBinding>(),
    CustomerDetailContract.CustomerDetailView, FeedAdapter.Wink {

    private lateinit var mBinding: ActivityCustomerDetailBinding

    private var customerDetail: ArrayList<ContactRandomData>? = null
    private var customerDetailResponse: ContactRandomData? = null
    private var alertDialog: AlertDialog? = null
    private var progressBar: ProgressBar? = null
    private lateinit var presenter: CustomerDetailPresenter
    private var followPersonName: String = ""
    private var myUniqueName = ""
    private var followSingleTask: Boolean = true
    private var winkSingleTask : Boolean = true

    private lateinit var feedAdapter: FeedAdapter

    private var userList: SignUpModel? = SignUpModel()

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_customer_detail

    override fun initUI(binding: ActivityCustomerDetailBinding) {
        mBinding = binding

        myUniqueName = PreferanceRepository.getString(Constant.uniqueName)

        if (mAuth.currentUser == null) {
            if (followSingleTask) {
                followSingleTask = false
                setDialog()
            }
        }

        feedAdapter = FeedAdapter(this, this)
        presenter = CustomerDetailPresenter(this@CustomerDetailActivity)

        userList = intent.getSerializableExtra(Constant.uniqueName) as SignUpModel
        followPersonName = userList?.userName!!

        setUpRecyclerView()



        mBinding.toolbar.apply { setUpToolbar() }

        setDetailPage()

        mBinding.txtFollow.setOnClickListener {
            follow()
        }
        mBinding.imgUnFollow.setOnClickListener {
            unFollow()
        }
        //presenter.customerDetail(nameId!!)
    }

    private fun unFollow() {
        presenter.unFollow(followPersonName, myUniqueName)
        showToast("UnFollow")
    }

    private fun follow() {
        presenter.follow(followPersonName, myUniqueName)
    }

    private fun setUpRecyclerView() {
        mBinding.rvFeed.adapter = feedAdapter
        mBinding.rvFeed.setHasFixedSize(true)
    }

    private fun setDetailPage() {
        Glide.with(this).load(userList?.profilePic).into(mBinding.imgUser)
        mBinding.txtNameAge.text = "${userList?.name}, ${userList?.age}"
        mBinding.txtIAm.text = userList?.gender!![0]
        mBinding.txtHeight.text = userList?.height

        if (userList?.follower != null) {
            userList?.follower?.forEach {
                if (myUniqueName == it) {
                    mBinding.txtFollow.visibility = View.GONE
                    mBinding.imgUnFollow.visibility = View.VISIBLE
                } else {
                    mBinding.txtFollow.visibility = View.VISIBLE
                    mBinding.imgUnFollow.visibility = View.GONE
                }
            }
        }
    }

    override fun getCustomerDetail(p0: DataSnapshot) {
        customerDetail = ArrayList()
        for (userData: DataSnapshot in p0.children.iterator()) {
            customerDetailResponse = userData.getValue(ContactRandomData::class.java)
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

    override fun followDone() {
        followSingleTask = true
        mBinding.txtFollow.visibility = View.GONE
        mBinding.imgUnFollow.visibility = View.VISIBLE
        showToast("you follow $followPersonName")
    }


    override fun unFollowDone() {
        mBinding.txtFollow.visibility = View.VISIBLE
        mBinding.imgUnFollow.visibility = View.GONE
        showToast("UnFollow $followPersonName")
    }

    override fun followError(message: String) {
        showToast(message)
    }

    override fun addWink(position: Int?) {
        if(winkSingleTask){
            winkSingleTask = false

        }

        showToast("Add Wink")
    }
    override fun winkAdded() {
        winkSingleTask = true
    }

    override fun winkRemove() {
        winkSingleTask = true
    }
}