package com.zgame.zgame.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log.e
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.adapter.CustomerAdapter
import com.zgame.zgame.adapter.CustomerContactAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerContract
import com.zgame.zgame.databinding.FragmentUserGalleryBinding
import com.zgame.zgame.imageStories.StoriesProgressView
import com.zgame.zgame.model.CircleData
import com.zgame.zgame.model.ContactRandomData
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.CustomerPresenter
import com.zgame.zgame.utils.Constant


class CustomerGalleryFragment : BaseFragment<FragmentUserGalleryBinding>(), CustomerAdapter.Profile,
    CustomerContract.CustomerView , CustomerContactAdapter.StoriesCallBack,
    StoriesProgressView.StoriesListener {

    private var customerAdapter: CustomerAdapter? = null
    private var contactAdapter: CustomerContactAdapter? = null
    private var allCustomerResponse: ArrayList<ContactRandomData>? = null
    private lateinit var presenter: CustomerPresenter
    private var userLists: ArrayList<SignUpModel>? = null
    private var alertDialog: AlertDialog? = null
    private var storyImages : ImageView ? = null
    private var storyCounter = 0
    private var circleProfileData : CircleData? = null
    private var pressTime = 0L
    private var limit = 500L
    private var stories : StoriesProgressView? = null
    private var options: RequestOptions? = null


    private val displayMetrics = DisplayMetrics()
    private var heightOfAlertDialog : Int = 0
    private var widthOfAlertDialog : Int = 0

    private lateinit var mInterstitialAd: InterstitialAd

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding
        MobileAds.initialize(activity, resources.getString(R.string.full_screen_unit_app_id))

        val adRequest = AdRequest.Builder().build()
        mBinding.addView.loadAd(adRequest)

        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
         heightOfAlertDialog = displayMetrics.heightPixels - 100
         widthOfAlertDialog = displayMetrics.widthPixels - 50


        options = RequestOptions().override(widthOfAlertDialog, heightOfAlertDialog)


        mBinding.addView.adListener = object: AdListener() {
            override fun onAdLoaded() {
               e("sdfsdfs", "Loded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                e("sdfsdfs", "Failed $errorCode")
            }

            override fun onAdOpened() {
                e("sdfsdfs", "open")
            }

            override fun onAdClicked() {
                e("sdfsdfs", "click")
            }

            override fun onAdLeftApplication() {
                e("sdfsdfs", "close app")
            }

            override fun onAdClosed() {
                e("sdfsdfs", "AddClose")
            }
        }



        //showAdd()


        presenter = CustomerPresenter(this)
        //mBinding.pulsator.start()

        initRecyclerView()

        (activity as MainActivity).permission()
        allCustomerResponse = ArrayList()

        /*else {
             presenter.customerRandomList()
         }*/

        presenter.circleProfile()


    }


    private fun initRecyclerView() {
        mBinding.rvCustomers.layoutManager = GridLayoutManager(activity, 3)
        customerAdapter = CustomerAdapter(activity, this)
        mBinding.rvCustomersContact.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        contactAdapter = CustomerContactAdapter(context = context!!, callBack = this)

    }

    override fun initNav(view: View) {
    }

    override fun userDetailPage(position: Int?) {
        startActivity(
            Intent(
                activity,
                CustomerDetailActivity::class.java
            ).putExtra(Constant.uniqueName, userLists!![position!!])
        )
    }

    override fun getCustomerRandomList(p0: DataSnapshot) {
        for (userData: DataSnapshot in p0.children.iterator()) {
            //allCustomerResponse?.add(userData.getValue(ContactRandomData::class.java)!!)
        }
    }

    override fun getUsersFilterList(userFilterList: ArrayList<SignUpModel>?) {
        userLists = ArrayList()
        mBinding.rvCustomers.visibility = View.VISIBLE
        // mBinding.cvGif.visibility = View.GONE
        //mBinding.pulsator.stop()
        for (i in userFilterList!!.indices) {
            if (userFilterList[i].userName != PreferanceRepository.getString(Constant.uniqueName)) {
                userLists?.add(userFilterList[i])
            }
        }

        if (userLists?.size == 0) {
            showToast("No data available")
        } else {

            customerAdapter?.addItem(userLists)
            mBinding.rvCustomers.adapter = customerAdapter
        }
    }

    override fun getNullValue(message: String) {
        showToast(message)
    }

    override fun setContactImages(images: ArrayList<String>) {

    }

    override fun getCircleProfileData(circleProfile: ArrayList<CircleData>?) {
        mBinding.rvCustomersContact.visibility = View.VISIBLE
        contactAdapter?.addItem(circleProfile)
        mBinding.rvCustomersContact.adapter = contactAdapter
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            if (userLists == null) {
                presenter.usersFilterList()
            }
        }
        (activity as MainActivity).setUserImage(
            PreferanceRepository.getString(Constant.uniqueName),
            PreferanceRepository.getString(Constant.profilePic)
        )
    }

    override fun openStoriesDialog(circleProfileData: CircleData) {
        this.circleProfileData = circleProfileData
        storyCounter = 0
        val dialogBuilder = AlertDialog.Builder(activity)
        val layoutView: View = layoutInflater.inflate(R.layout.dialog_stories, null)
        storyImages  = layoutView.findViewById(R.id.imageView)
        stories  = layoutView.findViewById(R.id.stories)
        val next = layoutView.findViewById<View>(R.id.skip)
        val reverse = layoutView.findViewById<View>(R.id.reverse)

        stories?.setStoriesCount(circleProfileData.listOfImages!!.size)
        stories?.setStoryDuration(3000L)
        stories?.setStoriesListener(this)
        stories?.startStories(storyCounter)

        next.setOnClickListener{
            stories?.skip()
        }
        next.setOnTouchListener(onTouchListener)
        reverse.setOnClickListener {
            stories?.reverse()
        }
        reverse.setOnTouchListener(onTouchListener)


        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()

        alertDialog?.window?.setLayout(widthOfAlertDialog, heightOfAlertDialog)

        Glide.with(activity!!).load(circleProfileData.listOfImages!![storyCounter]).apply( options!!).into(storyImages!!)
        alertDialog?.setCancelable(true)

    }

    override fun onComplete() {
        alertDialog?.dismiss()
    }

    override fun onPrev() {
        if (storyCounter - 1 < 0) {return}else{ Glide.with(activity!!).load(circleProfileData?.listOfImages!![--storyCounter]).apply( options!!).into(storyImages!!)}

    }

    override fun onNext() {
        Glide.with(activity!!).load(circleProfileData?.listOfImages!![++storyCounter]).apply(options!!).into(storyImages!!)
    }


    private val onTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }



    /* private fun showAdd() {
         MobileAds.initialize(activity, resources.getString(R.string.full_screen_unit_app_id))
         mInterstitialAd = InterstitialAd(activity)
         mInterstitialAd.adUnitId = resources.getString(R.string.full_screen_unit_id)
         mInterstitialAd.loadAd(AdRequest.Builder().build())

         if (mInterstitialAd.isLoaded) {
             mInterstitialAd.show()
         } else {
             Log.e("TAG", "The interstitial wasn't loaded yet.")
         }


         mInterstitialAd.adListener = object: AdListener() {
             override fun onAdLoaded() {
                 // Code to be executed when an ad finishes loading.
             }

             override fun onAdFailedToLoad(errorCode: Int) {
                 // Code to be executed when an ad request fails.
             }

             override fun onAdOpened() {
                 // Code to be executed when the ad is displayed.
             }

             override fun onAdClicked() {
                 // Code to be executed when the user clicks on an ad.
             }

             override fun onAdLeftApplication() {
                 // Code to be executed when the user has left the app.
             }

             override fun onAdClosed() {
                 // Code to be executed when the interstitial ad is closed.
             }
         }
     }*/
}