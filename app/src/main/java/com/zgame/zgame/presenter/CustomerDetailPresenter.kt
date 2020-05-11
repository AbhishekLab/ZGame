package com.zgame.zgame.presenter

import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.base.BaseActivity.Companion.mAuth
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Validation
import java.lang.reflect.Field


class CustomerDetailPresenter(private val view: CustomerDetailContract.CustomerDetailView) :
    CustomerDetailContract.CustomerDetailPresenter {

    private var databaseRef: DatabaseReference? = null
    private var ref: Query? = null
    private var context: CustomerDetailActivity = view as CustomerDetailActivity
    private var db: FirebaseFirestore? = null

    override fun dialogLogin(email: String, password: String) {
        if (Validation.emailValidation(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser != null) {
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

    override fun wink(myUniqueName: String) {
        db = FirebaseFirestore.getInstance()

        db!!.collection(Constant.DbName).document(myUniqueName)
            .update("wink", FieldValue.arrayUnion(myUniqueName)).addOnCompleteListener {
        }.addOnFailureListener {
            context.followError(it.message!!)
        }

    }

    override fun winkRemove(myUniqueName: String) {
        db = FirebaseFirestore.getInstance()

        db!!.collection(Constant.DbName).document(myUniqueName)
            .update("wink", FieldValue.arrayRemove(myUniqueName)).addOnCompleteListener {
            }.addOnFailureListener {
                context.followError(it.message!!)
            }
    }

    override fun follow(follow: String?, follower: String) {
        db = FirebaseFirestore.getInstance()
        val batch: WriteBatch = db!!.batch()

        val myFollow = db!!.collection(Constant.DbName).document(follower)
        batch.update(myFollow, "following", FieldValue.arrayUnion(follow))

        val myFollower = db!!.collection(Constant.DbName).document(follow!!)
        batch.update(myFollower, "follower", FieldValue.arrayUnion(follower))

        batch.commit().addOnCompleteListener {
            context.followDone()
        }.addOnFailureListener {
            context.followError(it.message!!)
        }
    }

    override fun unFollow(follow: String?, follower: String) {
        db = FirebaseFirestore.getInstance()
        val batch: WriteBatch = db!!.batch()

        val myFollow = db!!.collection(Constant.DbName).document(follower)
        batch.update(myFollow, "following", FieldValue.arrayRemove(follow))

        val myFollower = db!!.collection(Constant.DbName).document(follow!!)
        batch.update(myFollower, "follower", FieldValue.arrayRemove(follower))

        batch.commit().addOnCompleteListener {
            context.unFollowDone()
        }.addOnFailureListener {
            context.followError(it.message!!)
        }
    }
}