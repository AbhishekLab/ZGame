package com.zgame.zgame.presenter

import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.zgame.zgame.R
import com.zgame.zgame.activity.CustomerDetailActivity
import com.zgame.zgame.base.BaseActivity.Companion.mAuth
import com.zgame.zgame.contract.CustomerDetailContract
import com.zgame.zgame.model.PostModel
import com.zgame.zgame.model.UpdateProfileModel
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Validation


class CustomerDetailPresenter(private val view: CustomerDetailContract.CustomerDetailView) :
    CustomerDetailContract.CustomerDetailPresenter {

    private var databaseRef: DatabaseReference? = null
    private var ref: Query? = null
    private var context: CustomerDetailActivity = view as CustomerDetailActivity
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private var images : ArrayList<String>? = null
    private var postModel = PostModel()

    private var profileDetail : UpdateProfileModel = UpdateProfileModel()

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

    override fun customerDetail(userName: String) {
        db!!.collection(Constant.DbName).document(userName).collection(userName).document(Constant.firebaseUserGallery).get().addOnCompleteListener {
            if (it.result?.data.isNullOrEmpty()) {
                view.error(context.getString(R.string.no_data_found))
            } else {
                profileDetail = it.result?.toObject(UpdateProfileModel::class.java)!!
                view.getCustomerDetail(profileDetail)
            }

        }?.addOnFailureListener {
            view.error("Api Not Working")
        }

    }

    override fun wink(myUniqueName: String) {
        db!!.collection(Constant.DbName).document(myUniqueName)
            .update("wink", FieldValue.arrayUnion(myUniqueName)).addOnCompleteListener {
        }.addOnFailureListener {
            context.error(it.message!!)
        }

    }

    override fun winkRemove(myUniqueName: String) {
        db!!.collection(Constant.DbName).document(myUniqueName)
            .update("wink", FieldValue.arrayRemove(myUniqueName)).addOnCompleteListener {
            }.addOnFailureListener {
                context.error(it.message!!)
            }
    }

    override fun follow(follow: String?, follower: String) {
        val batch: WriteBatch = db!!.batch()

        val myFollow = db!!.collection(Constant.DbName).document(follower)
        batch.update(myFollow, "following", FieldValue.arrayUnion(follow))

        val myFollower = db!!.collection(Constant.DbName).document(follow!!)
        batch.update(myFollower, "follower", FieldValue.arrayUnion(follower))

        batch.commit().addOnCompleteListener {
            context.followDone()
        }.addOnFailureListener {
            context.error(it.message!!)
        }
    }

    override fun unFollow(follow: String?, follower: String) {
        val batch: WriteBatch = db!!.batch()

        val myFollow = db!!.collection(Constant.DbName).document(follower)
        batch.update(myFollow, "following", FieldValue.arrayRemove(follow))

        val myFollower = db!!.collection(Constant.DbName).document(follow!!)
        batch.update(myFollower, "follower", FieldValue.arrayRemove(follower))

        batch.commit().addOnCompleteListener {
            context.unFollowDone()
        }.addOnFailureListener {
            context.error(it.message!!)
        }
    }

    override fun getSelectedUserImage(uniqueName: String) {
        images = ArrayList()
        db?.collection(Constant.DbName)?.document(uniqueName)?.collection(uniqueName)?.document(Constant.firebaseGallery)?.get()?.addOnCompleteListener {
            if(it.isSuccessful){
                if(it.result!!.exists()){
                    postModel = it.result?.toObject(PostModel::class.java)!!
                        context.setSelectedUserImage(postModel)

                }
            }
        }?.addOnFailureListener {
            context.error(it.message!!)
        }

    }
}