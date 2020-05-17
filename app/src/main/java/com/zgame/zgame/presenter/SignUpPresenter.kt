package com.zgame.zgame.presenter

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.zgame.zgame.activity.SignUp3Activity
import com.zgame.zgame.base.BaseActivity.Companion.mAuth
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.SignUpContract
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant.DbName
import com.zgame.zgame.utils.Constant.dbUserName
import com.zgame.zgame.utils.Constant.uniqueName
import java.util.regex.Pattern


class SignUpPresenter(view: SignUpContract.SignUpView) : SignUpContract.SignUpPresenter {

    private val context = view as SignUp3Activity
    private var db: FirebaseFirestore? = null
    private var query: Query? = null
    private var mStorageRef: StorageReference? = null
    private var userNameQuery: CollectionReference? = null
    private var userName : String? = null

    override fun doSignUp(
        signUpModel: SignUpModel, profilePhoto: Uri?
    ) {

        mStorageRef = FirebaseStorage.getInstance().reference
        db = FirebaseFirestore.getInstance()
        userName = signUpModel.userName


        mAuth.createUserWithEmailAndPassword(signUpModel.email!!, signUpModel.password!!)
            .addOnCompleteListener(context) { it ->
                if (it.isSuccessful) {
                    uploadProfilePic(profilePhoto, signUpModel)
                } else{
                    context.registerNotDone(it.exception?.message)
            }
            }
    }

    private fun uploadProfilePic(profilePhoto: Uri?, signUpModel: SignUpModel) {
        if(profilePhoto!=null){
            mStorageRef?.child(signUpModel.userName!!)?.child("ProfileImage")?.child("profilePic")
                ?.putFile(profilePhoto)?.addOnCompleteListener { it ->
                    if(it.isSuccessful){
                        mStorageRef?.child(signUpModel.userName!!)?.child("ProfileImage")?.child("profilePic")?.downloadUrl?.addOnCompleteListener {
                            if(it.isSuccessful) {
                                signUpModel.profilePic = it.result.toString()
                                dataUpload(signUpModel)
                            }else{
                                dataUpload(signUpModel)
                            }
                        }?.addOnFailureListener {
                            dataUpload(signUpModel)
                        }
                    }else{
                        dataUpload(signUpModel)
                    }
                }?.addOnFailureListener {
                    dataUpload(signUpModel)
                }
        }else{
            dataUpload(signUpModel)
        }


        /*if(profilePhoto!=null) {
            mStorageRef?.child(signUpModel.userName!!)?.child("ProfileImage")?.child("profilePic")
                ?.putFile(profilePhoto)?.addOnSuccessListener {
                    mStorageRef?.downloadUrl?.addOnSuccessListener {
                        signUpModel.profilePic = it.toString()
                        dataUpload(signUpModel)
                    }?.addOnFailureListener {
                        d("errorsdfsdfs", it.message.toString())
                        dataUpload(signUpModel)
                    }

                }?.addOnFailureListener {
                    dataUpload(signUpModel)
                }
        }else{
            dataUpload(signUpModel)
        }*/
    }











    private fun dataUpload(signUpModel: SignUpModel) {
        db?.collection(DbName)?.document(signUpModel.userName!!)?.set(signUpModel)
            ?.addOnSuccessListener {
                PreferanceRepository.setString(uniqueName, signUpModel.userName!!)
                context.registerDone()
            }?.addOnFailureListener {
                context.registerNotDone(it.message)
            }
    }

    override fun checkUserName(uniqueName: String) {
        db = FirebaseFirestore.getInstance()
        userNameQuery = db?.collection(DbName)
        query = userNameQuery?.whereEqualTo(dbUserName, uniqueName)
        query?.get()?.addOnSuccessListener {
            if (it.documents.size == 0) {
                context.uniqueNameInCorrect()
            } else {
                context.uniqueNameCorrect()
            }
        }?.addOnFailureListener {
            context.databaseError(it.message!!)
        }
    }

    fun emailValidation(email: String): Boolean {
        return if (email.isBlank()) {
            false
        } else
            Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
    }
}