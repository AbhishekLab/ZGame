package com.zgame.zgame.presenter

import android.util.Log.e
import com.google.firebase.firestore.FirebaseFirestore
import com.zgame.zgame.contract.UpdateProfileContract
import com.zgame.zgame.model.UpdateProfileModel
import com.zgame.zgame.model.UserProfileGalleryModel
import com.zgame.zgame.utils.Constant

class UpdateProfilePresenter(
    private val view: UpdateProfileContract.UpdateProfileView,
    private val uniqueName: String
) : UpdateProfileContract.UpdateProfilePresenter {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var response: UpdateProfileModel? = null

    override fun updateProfile(model: UpdateProfileModel) {

        db.collection(Constant.DbName).document(uniqueName).collection(uniqueName)
            .document(Constant.firebaseUserGallery).set(model)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.updateSuccessfully("Yoooo!!! Update Successfully")
                }
            }.addOnFailureListener {
                view.error("Something is wrong ${it.message.toString()}")
            }.addOnCanceledListener {
                view.error("Update cancel")
            }
    }

    override fun getUserProfileData() {
        response = UpdateProfileModel()
        db.collection(Constant.DbName).document(uniqueName).collection(uniqueName)
            .document(Constant.firebaseUserGallery).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    response = it.result?.toObject(UpdateProfileModel::class.java)
                    if (response != null) {
                        view.setUserProfileData(response!!)
                    }
                } else {
                    view.error("No Data Found")
                }
            }.addOnFailureListener {
                view.error("Something is wrong ${it.message.toString()}")
            }.addOnCanceledListener {
                view.error("Update cancel")
            }
    }


}