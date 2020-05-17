package com.zgame.zgame.fragment

import android.content.Intent
import android.util.Log.e
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.zgame.zgame.model.ContactRandomData
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.CustomerPresenter
import com.zgame.zgame.utils.Constant


class CustomerGalleryFragment : BaseFragment<FragmentUserGalleryBinding>(), CustomerAdapter.Profile,
    CustomerContract.CustomerView {

    private var customerAdapter: CustomerAdapter? = null
    private var contactAdapter: CustomerContactAdapter? = null
    private var allCustomerResponse: ArrayList<ContactRandomData>? = null
    private lateinit var presenter: CustomerPresenter
    private var userLists: ArrayList<SignUpModel>? = null

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding

        presenter = CustomerPresenter(this)
        mBinding.pulsator.start()

        


        initRecyclerView()

        (activity as MainActivity).permission()
        allCustomerResponse = ArrayList()

        /*else {
             presenter.customerRandomList()
         }*/

        presenter.getContactRandomImages()
        //presenter.customerRandomList()


    }

    private fun initRecyclerView() {
        mBinding.rvCustomers.layoutManager = GridLayoutManager(activity, 3)
        customerAdapter = CustomerAdapter(activity, this)
        mBinding.rvCustomersContact.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        contactAdapter = CustomerContactAdapter(context = context!!)


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
            allCustomerResponse?.add(userData.getValue(ContactRandomData::class.java)!!)
        }
    }

    override fun getUsersFilterList(userFilterList: ArrayList<SignUpModel>?) {
        userLists = ArrayList()
        mBinding.rvCustomers.visibility = View.VISIBLE
        mBinding.cvGif.visibility = View.GONE
        mBinding.pulsator.stop()
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
        mBinding.rvCustomersContact.visibility = View.VISIBLE
        contactAdapter?.addItem(images)
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
}