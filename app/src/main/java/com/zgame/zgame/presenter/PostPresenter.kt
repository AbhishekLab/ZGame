package com.zgame.zgame.presenter

import android.net.Uri
import android.util.Log.e
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zgame.zgame.contract.PostContract
import com.zgame.zgame.model.PostModel
import com.zgame.zgame.model.PostModelRequestImagesLikes
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.firebaseGallery
import com.zgame.zgame.utils.Constant.firebaseUserGallery

class PostPresenter(private val view: PostContract.PostView) : PostContract.PostPresenter {

    private var mStorageRef: StorageReference? = null
    private var randomImageName: String = ""
    private var db: FirebaseFirestore? = null
    private var imageUrl : String = ""
    private var postModel : PostModel = PostModel()

    override fun postImage(imageFile: Uri?, userUniqueName: String?) {
        if (imageFile != null && userUniqueName != null) {
            randomImageName = System.currentTimeMillis().toString()
            uploadProfilePic(imageFile, userUniqueName, randomImageName)
        } else {
            view.postFailed("file and fileName not supported")
        }
    }

    private fun uploadProfilePic(profilePhoto: Uri?, userUniqueName: String, randomImageName: String) {
        mStorageRef = FirebaseStorage.getInstance().reference
        mStorageRef?.child(userUniqueName)?.child(firebaseUserGallery)?.child("$userUniqueName$randomImageName")
            ?.putFile(profilePhoto!!)?.addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    mStorageRef?.child(userUniqueName)?.child(firebaseUserGallery)?.child("$userUniqueName$randomImageName")?.downloadUrl
                        ?.addOnCompleteListener {
                            if(it.isSuccessful){
                                //    postModel.image?.put(randomImageName , it.result.toString())
                                imageUrl = it.result.toString()
                                addUrlToUserDatabase(imageUrl,userUniqueName,randomImageName)
                            }

                         }?.addOnFailureListener {
                                //using database in worst case
                                view.postFailed("Url not attached to database")
                        }
                } else {
                    view.postFailed(it.exception?.message!!)
                }
            }?.addOnFailureListener {
                view.postFailed(it.message!!)
            }
    }

    private fun addUrlToUserDatabase(
        imageUrl: String,
        userUniqueName: String,
        randomImageName: String
    ) {
        db = FirebaseFirestore.getInstance()
       /* val hashMap:HashMap<String,String> = HashMap()
        hashMap["$userUniqueName$randomImageName"] = imageUrl*/

        val imgObj = PostModelRequestImagesLikes()
        imgObj.imageId = randomImageName
        imgObj.images = imageUrl

        db?.collection(Constant.DbName)?.document(userUniqueName)?.collection(userUniqueName)?.document(firebaseGallery)
            ?.update("image",FieldValue.arrayUnion(imgObj))
            ?.addOnCompleteListener {
                view.postSuccess()
            }?.addOnFailureListener {
                view.postFailed(it.message!!)
                e("Eror of Post", it.message.toString())
            }?.addOnCanceledListener {
                view.postFailed("Cancelled!!")
            }
    }
}