package com.zgame.zgame.presenter

import android.net.Uri
import android.util.Log.e
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zgame.zgame.contract.UpdateProfileContract
import com.zgame.zgame.model.UpdateProfileModel
import com.zgame.zgame.utils.Constant

class UpdateProfilePresenter(private val view: UpdateProfileContract.UpdateProfileView, private val uniqueName: String) : UpdateProfileContract.UpdateProfilePresenter {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var response: UpdateProfileModel? = null
    private var mStorageRef: StorageReference? = FirebaseStorage.getInstance().reference
    private var downloadedProfilePic : String? = ""

    override fun updateProfile(model: UpdateProfileModel, profilePhoto: Uri?) {
        if(profilePhoto!=null){
            mStorageRef?.child(uniqueName)?.child("ProfileImage")?.child("profilePic")
                ?.putFile(profilePhoto)?.addOnCompleteListener { it ->
                    if(it.isSuccessful){
                        mStorageRef?.child(uniqueName)?.child("ProfileImage")?.child("profilePic")?.downloadUrl?.addOnCompleteListener {
                            if(it.isSuccessful) {
                                downloadedProfilePic = it.result.toString()
                                updateWithBatch(model,downloadedProfilePic!!)
                            }else{
                                updateWithBatch(model,"")
                            }
                        }?.addOnFailureListener {
                            updateWithBatch(model,"")
                        }
                    }else{
                        updateWithBatch(model,"")
                    }
                }?.addOnFailureListener {
                    updateWithBatch(model,"")
                }
        }else{
            updateWithBatch(model,"")
        }
    }

    private fun updateWithBatch(model: UpdateProfileModel, downloadedProfilePic: String) {

        if(downloadedProfilePic.isNotEmpty()){
            val hashMap = HashMap<String,String>()
            hashMap[Constant.profilePic] = downloadedProfilePic

            val batch: WriteBatch = db.batch()
            val updateModelRequest = db.collection(Constant.DbName).document(uniqueName).collection(uniqueName).document(Constant.firebaseUserGallery)
            batch.set(updateModelRequest , model)

            db.collection(Constant.DbName).document(uniqueName).update(Constant.profilePic, downloadedProfilePic).addOnCompleteListener {
                if(it.isSuccessful){
                    e("Success", "Success")
                }
            }.addOnFailureListener {
                e("FailedDueToThisReason", it.message.toString())
            }

            batch.commit().addOnCompleteListener {
                if (it.isSuccessful) {
                    view.updateSuccessfully("Yoooo!!! Update Successfully")
                }else{
                    view.error("Something is wrong}")
                }
            }.addOnFailureListener {
                view.error("Something is wrong ${it.message.toString()}")
            }

        }else{
            db.collection(Constant.DbName).document(uniqueName).collection(uniqueName)
                .document(Constant.firebaseUserGallery).set(model)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        view.updateSuccessfully("Yoooo!!! Update Successfully")
                    }else{
                        view.error("Something is wrong}")
                    }
                }.addOnFailureListener {
                    view.error("Something is wrong ${it.message.toString()}")
                }.addOnCanceledListener {
                    view.error("Update cancel")
                }
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
                    }else{
                        view.error("No data found")
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