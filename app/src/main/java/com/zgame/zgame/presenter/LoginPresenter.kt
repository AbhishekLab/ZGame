package com.zgame.zgame.presenter

import android.util.Log
import com.anupcowkur.reservoir.Reservoir
import com.anupcowkur.reservoir.ReservoirPutCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.zgame.zgame.activity.LoginActivity
import com.zgame.zgame.base.BaseActivity.Companion.mAuth
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.LoginContract
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.DbName
import com.zgame.zgame.utils.Validation


class LoginPresenter(private val view: LoginContract.LoginView) : LoginContract.LoginPresenter {

    private var context: LoginActivity = view as LoginActivity
    private var db: FirebaseFirestore? = null
    private var email: String? = null
    private var currentUser : SignUpModel? = null

    override fun dialogLogin(email: String, password: String) {
        if (Validation.emailValidation(email, password)) {
            this.email = email
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context) { task ->
                    task.addOnSuccessListener {
                        if (it.user != null) {
                            getCurrentUserData()
                        }

                    }.addOnFailureListener {
                        view.loginFailed(it.message!!)
                    }
                }
        } else {
            view.loginFailed("Please enter valid Email Id")
        }
    }


    private fun getCurrentUserData() {
        db = FirebaseFirestore.getInstance()
        val userData = db?.collection(DbName)?.whereEqualTo(Constant.email, email)?.get()
        userData?.addOnSuccessListener {
            for (i in it.toList().indices) {
                currentUser = it.toList()[i].toObject(SignUpModel::class.java)
            }
9
            Reservoir.putAsync(Constant.reservoir_key, currentUser, object : ReservoirPutCallback {
                override fun onSuccess() {
                    PreferanceRepository.setString(Constant.uniqueName, currentUser?.userName!!)
                    PreferanceRepository.setString(Constant.name, currentUser?.name)
                    PreferanceRepository.setString(Constant.profilePic, currentUser?.profilePic.toString())
                }

                override fun onFailure(e: Exception) {
                    Log.e("Failed to", "Failed to save login data")
                }
            })
            view.loginSuccess()
        }?.addOnFailureListener {
            view.loginFailed(it.message!!)
        }
    }
}