package com.zgame.zgame.presenter

import com.google.firebase.firestore.FirebaseFirestore
import com.zgame.zgame.contract.UserProfileContract
import com.zgame.zgame.model.PostModel
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.firebaseUserGallery

class UserProfilePresenter(private val view: UserProfileContract.UserProfileView) :
    UserProfileContract.UserProfilePresenter {

    private var db: FirebaseFirestore? = null
    private var userProfileImages =  PostModel()

    override fun getUserImages(uniqueName: String?) {
        if (!uniqueName.isNullOrEmpty() && !uniqueName.isNullOrBlank()) {
            db = FirebaseFirestore.getInstance()
            val userImages =
                db?.collection(Constant.DbName)?.document(uniqueName)?.collection(uniqueName)
                    ?.document(firebaseUserGallery)?.get()

            userImages?.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.exists()) {
                        userProfileImages = it.result?.toObject(PostModel::class.java)!!
                    }else{
                        view.error("No Data Found")
                    }
                } else {
                    view.error(it.exception?.message.toString())
                }

                view.fetchSuccessfully(userProfileImages)


            }?.addOnFailureListener {
                view.error(it.message.toString())
            }?.addOnCanceledListener {
                view.error("Canceled!!")
            }
        } else {
            view.error("Please login again")
        }
    }
}