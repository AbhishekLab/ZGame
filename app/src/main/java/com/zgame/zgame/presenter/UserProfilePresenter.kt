package com.zgame.zgame.presenter

import com.google.firebase.firestore.FirebaseFirestore
import com.zgame.zgame.contract.UserProfileContract
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.firebaseUserGallery

class UserProfilePresenter(private val view : UserProfileContract.UserProfileView) : UserProfileContract.UserProfilePresenter{

    private var db: FirebaseFirestore? = null
    private var imageUrl : ArrayList<String>? = null

    override fun getUserImages(uniqueName: String?) {
        if(!uniqueName.isNullOrEmpty() && !uniqueName.isNullOrBlank()){
            imageUrl = ArrayList()
            db = FirebaseFirestore.getInstance()
            val userImages = db?.collection(Constant.DbName)?.document(uniqueName)?.collection(uniqueName)?.document(firebaseUserGallery)?.get()

            userImages?.addOnCompleteListener {
                if(it.isSuccessful){
                    if(it.result!=null){
                        if(it.result?.data!=null)
                        for(i in it.result!!.data!!){
                            imageUrl?.add(i.value.toString())
                        }else{
                            view.error("Something went wrong")
                        }
                    }else{
                        view.error("Something went wrong")
                    }
                }else{
                    view.error(it.exception?.message.toString())
                }

                view.fetchSuccessfully(imageUrl!!)


            }?.addOnFailureListener {
                view.error(it.message.toString())
            }?.addOnCanceledListener {
                view.error("Canceled!!")
            }
        }else{
            view.error("Please login again")
        }
    }
}