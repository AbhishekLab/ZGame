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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.Transition
import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.R
import com.zgame.zgame.adapter.FeedAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.databinding.ActivityCustomerDetailBinding
import com.zgame.zgame.model.ContactRandomData
import com.zgame.zgame.model.PostModel
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.model.UpdateProfileModel
import com.zgame.zgame.presenter.CustomerDetailPresenter
import com.zgame.zgame.utils.Constant
import kotlinx.android.synthetic.main.activity_customer_detail.view.*


class CustomerDetailActivity : BaseActivity<ActivityCustomerDetailBinding>(),
    CustomerDetailContract.CustomerDetailView, FeedAdapter.Wink {

    private lateinit var mBinding: ActivityCustomerDetailBinding

    private var alertDialog: AlertDialog? = null
    private var progressBar: ProgressBar? = null
    private lateinit var presenter: CustomerDetailPresenter
    private var followPersonName: String = ""
    private var myUniqueName = ""
    private var followSingleTask: Boolean = true
    private var winkSingleTask : Boolean = true
    private var images : ArrayList<String>? = null

    private var userProfileDetail : UpdateProfileModel? = null

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

        mBinding.rvFeed.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvFeed.adapter = feedAdapter
        images = ArrayList()

        presenter = CustomerDetailPresenter(this@CustomerDetailActivity)

        userList = intent.getSerializableExtra(Constant.uniqueName) as SignUpModel
        followPersonName = userList?.userName!!


        mBinding.toolbar.apply { setUpToolbar() }

        setDetailPage()
        setUserImages()



        mBinding.txtFollow.setOnClickListener {
            follow()
        }
        mBinding.imgUnFollow.setOnClickListener {
            unFollow()
        }

        mBinding.txtPost.setOnClickListener {
            mBinding.rvFeed.visibility = View.VISIBLE
            mBinding.llDetails.visibility = View.GONE
            feedAdapter.addItem(images)
            mBinding.txtPost.setTextColor(resources.getColor(R.color.dark_brown))
            mBinding.txtDetails.setTextColor(resources.getColor(R.color.light_gray))
        }

        mBinding.txtDetails.setOnClickListener {
            if(userProfileDetail==null){
                mBinding.progressBar.visibility = View.VISIBLE
                presenter.customerDetail(followPersonName)
            }

            mBinding.response = userList
            mBinding.rvFeed.visibility = View.GONE
            mBinding.llDetails.visibility = View.VISIBLE
            mBinding.txtDetails.setTextColor(resources.getColor(R.color.dark_brown))
            mBinding.txtPost.setTextColor(resources.getColor(R.color.light_gray))
        }

        mBinding.toolbar.img_back.setOnClickListener { finish() }
        //presenter.customerDetail(nameId!!)
    }

    private fun setUserImages() {
        mBinding.progressBar.visibility = View.VISIBLE
        presenter.getSelectedUserImage(uniqueName = followPersonName )
    }

    private fun unFollow() {
        presenter.unFollow(followPersonName, myUniqueName)
        showToast("UnFollow")
    }

    private fun follow() {
        presenter.follow(followPersonName, myUniqueName)
    }

    private fun setDetailPage() {
        Glide.with(this).load(userList?.profilePic).into(mBinding.imgUser)

        mBinding.imgUser.setTransitionListener(object : KenBurnsView.TransitionListener{
            override  fun onTransitionStart(transition: Transition?) {

            }
            override fun onTransitionEnd(transition: Transition?) {

            }
        })

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

    override fun getCustomerDetail(userProfileDetail: UpdateProfileModel) {
        this.userProfileDetail = userProfileDetail
        mBinding.userProfile =   this.userProfileDetail
        mBinding.progressBar.visibility = View.GONE

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
        //mBinding.ivBack.setOnClickListener { finish() }
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

    override fun error(message: String) {
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

    override fun setSelectedUserImage(userImage: PostModel) {
        mBinding.progressBar.visibility = View.GONE
        userImage.image?.map { it ->
            it.mapValues {
                images?.add(it.value)
            }
        }
        if(images?.size != 0){
            mBinding.rvFeed.visibility = View.VISIBLE
            mBinding.llDetails.visibility = View.GONE
            feedAdapter.addItem(images)
            mBinding.txtPost.setTextColor(resources.getColor(R.color.dark_brown))
        }else{
            mBinding.progressBar.visibility = View.VISIBLE
            mBinding.response = userList
            mBinding.rvFeed.visibility = View.GONE
            mBinding.llDetails.visibility = View.VISIBLE
            mBinding.txtDetails.setTextColor(resources.getColor(R.color.dark_brown))
            presenter.customerDetail(followPersonName)
        }
    }
}