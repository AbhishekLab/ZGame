package com.zgame.zgame.presenter

import com.google.firebase.database.*
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.utils.Validation

class CustomerDetailPresenter(private val view:CustomerDetailContract.CustomerDetailView) : CustomerDetailContract.CustomerDetailPresenter {

    private var databaseRef: DatabaseReference? = null
    private var ref: Query? = null
    private var context: CustomerDetailActivity = view as CustomerDetailActivity

    override fun dialogLogin(email: String, password: String) {
        if (Validation.emailValidation(email, password)) {
            context.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        if (context.mAuth.currentUser != null) {
                            view.loginSuccess()
                        } else {
                            view.loginFailed("Something went wrong, Please try again later")
                        }
                    } else {
                        view.loginFailed("Please check your credential")
                    }
                }
        } else {
            view.loginFailed("Please enter valid Email Id")
        }
    }

    override fun customerDetail(id: String) {
        databaseRef = FirebaseDatabase.getInstance().reference.child("Customers")
        databaseRef.let {
            ref = FirebaseDatabase.getInstance().getReference("Customers").orderByChild("id")
                .equalTo(id)
            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    view.getCustomerDetail(p0)
                }
            })
        }
    }

    override fun wink() {

    }
}