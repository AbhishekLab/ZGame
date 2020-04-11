package com.zgame.zgame.presenter

import android.util.Log.d
import com.google.firebase.database.*
import com.zgame.zgame.contract.CustomerContract

class CustomerPresenter (private val view: CustomerContract.CustomerView) : CustomerContract.CustomerPresenter {

    private var databaseRef: DatabaseReference? = null

    override fun customerList() {
        databaseRef = FirebaseDatabase.getInstance().getReference("Customers")

            databaseRef!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                d("fsfdf","error " +p0.message)
                }
                override fun onDataChange(p0: DataSnapshot) {
                view.getCustomerList(p0)
                }
            })
    }
}