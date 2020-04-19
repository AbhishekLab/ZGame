package com.zgame.zgame.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.adapter.CustomerAdapter
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerContract
import com.zgame.zgame.databinding.FragmentUserGalleryBinding
import com.zgame.zgame.model.CustomerData
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.presenter.CustomerPresenter
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.seeking

class CustomerGalleryFragment : BaseFragment<FragmentUserGalleryBinding>(), CustomerAdapter.Profile, CustomerContract.CustomerView {

    private var customerAdapter : CustomerAdapter? = null
    private var allCustomerResponse : ArrayList<CustomerData>? = null
    private lateinit var presenter : CustomerPresenter
    private var userLists: ArrayList<SignUpModel>? = ArrayList()

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding

        presenter = CustomerPresenter(this)

        (activity as MainActivity).permission()
        allCustomerResponse = ArrayList()

        if (mAuth.currentUser != null) {
            presenter.usersFilterList()
        } else {
            presenter.customerRandomList()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.rvCustomers.layoutManager = GridLayoutManager(activity, 2)
        customerAdapter = CustomerAdapter(activity,this)

    }

    override fun initNav(view: View) {
    }

    override fun itemListener(id: String?) {
        startActivity(Intent(activity,CustomerDetailActivity::class.java).putExtra("id",id))
    }

    override fun getCustomerRandomList(p0: DataSnapshot) {
        for (userData: DataSnapshot in p0.children.iterator()) {
            allCustomerResponse?.add(userData.getValue(CustomerData::class.java)!!)
        }
        /*customerAdapter?.addItem(allCustomerResponse)
        mBinding.rvCustomers.adapter = customerAdapter*/
    }

    override fun getUsersFilterList(userFilterList: ArrayList<SignUpModel>?) {
        for(i in userFilterList!!.indices){
            if(userFilterList[i].userName != PreferanceRepository.getString(Constant.uniqueName)){
                userLists?.add(userFilterList[i])
            }
        }

        if(userLists?.size == 0){
            showToast("No data available")
        }else{
            customerAdapter?.addItem(userLists)
            mBinding.rvCustomers.adapter = customerAdapter
        }


    }

    override fun getNullValue(message: String) {
        showToast(message)
    }
}