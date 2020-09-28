package com.zgame.zgame.presenter

import com.google.firebase.firestore.FirebaseFirestore
import com.zgame.zgame.contract.UserProfileContract
import com.zgame.zgame.model.ImageObj
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.firebaseGallery

class UserProfilePresenter(private val view: UserProfileContract.UserProfileView) :
    UserProfileContract.UserProfilePresenter {

    private var db: FirebaseFirestore? = null
    private var userProfileImages =  ImageObj()

    override fun getUserImages(uniqueName: String?) {
        if (!uniqueName.isNullOrEmpty() && !uniqueName.isNullOrBlank()) {
            db = FirebaseFirestore.getInstance()
            val userImages =
                db?.collection(Constant.DbName)?.document(uniqueName)?.collection(uniqueName)
                    ?.document(firebaseGallery)?.get()

            userImages?.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.exists()) {
                        userProfileImages = (it.result?.toObject(ImageObj::class.java)!!)

                        view.fetchSuccessfully(userProfileImages)

                    }else{
                        view.error("No Data Found")
                    }
                } else {
                    view.error(it.exception?.message.toString())
                }

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