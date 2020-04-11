package com.zgame.zgame.fragment

import android.content.Intent
import android.util.Log.d
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.adapter.CustomerAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.contract.CustomerContract
import com.zgame.zgame.databinding.FragmentUserGalleryBinding
import com.zgame.zgame.model.CustomerData
import com.zgame.zgame.presenter.CustomerPresenter

class CustomerGalleryFragment : BaseFragment<FragmentUserGalleryBinding>(), CustomerAdapter.Profile, CustomerContract.CustomerView {

    private var customerAdapter : CustomerAdapter? = null
    private var allCustomerResponse : ArrayList<CustomerData>? = null
    private lateinit var presenter : CustomerPresenter

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding

        presenter = CustomerPresenter(this)

        (activity as MainActivity).permission()
        allCustomerResponse = ArrayList()

        presenter.customerList()

        initRecyclerView()

        if ((activity as MainActivity).checkAuthInstance()) {
            fetchUserResources()
        } else {
            fetchRandomResources()
        }
    }

    private fun initRecyclerView() {
        mBinding.rvCustomers.layoutManager = GridLayoutManager(activity, 2)
        customerAdapter = CustomerAdapter(activity,this)

    }

    private fun fetchRandomResources() {
        d("response", "fetchRandomResources")
    }

    private fun fetchUserResources() {
        d("response", "fetchUserResources")
    }

    override fun initNav(view: View) {
    }

    override fun itemListener(id: String?) {
        startActivity(Intent(activity,CustomerDetailActivity::class.java).putExtra("id",id))
    }

    override fun getCustomerList(p0: DataSnapshot) {
        for (userData: DataSnapshot in p0.children.iterator()) {
            allCustomerResponse?.add(userData.getValue(CustomerData::class.java)!!)
        }
        customerAdapter?.addItem(allCustomerResponse)
        mBinding.rvCustomers.adapter = customerAdapter
    }

    override fun getNullValue(message: String) {
        showToast(message)
    }
}