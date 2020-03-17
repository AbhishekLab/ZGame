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
import com.zgame.zgame.databinding.FragmentUserGalleryBinding
import com.zgame.zgame.model.CustomerData

class CustomerGalleryFragment : BaseFragment<FragmentUserGalleryBinding>(), CustomerAdapter.Profile {

    private var databaseRef: DatabaseReference? = null
    private var customerAdapter : CustomerAdapter? = null
    private var allCustomerResponse : ArrayList<CustomerData>? = null

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding

        (activity as MainActivity).permission()
        allCustomerResponse = ArrayList()
        databaseRef = FirebaseDatabase.getInstance()?.reference?.child("Customers")

        databaseRef.let {
            it?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for (userData: DataSnapshot in p0.children.iterator()) {
                        allCustomerResponse?.add(userData.getValue(CustomerData::class.java)!!)
                    }
                    customerAdapter?.addItem(allCustomerResponse)
                    mBinding.rvCustomers.adapter = customerAdapter
                }
            })
        }

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
        /* val localFile = File.createTempFile("images", "jpg")
        riversRef.getFile(localFile)
            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                // Successfully downloaded data to local file
                // ...
            }).addOnFailureListener(OnFailureListener {
                // Handle failed download
                // ...
            })*/
        showToast("fetchUserResources")
        d("response", "fetchUserResources")
    }

    override fun initNav(view: View) {
    }

    override fun itemListener(adapterPosition: Int) {
        startActivity(Intent(activity,CustomerDetailActivity::class.java).putExtra("position",adapterPosition))
    }
}