package com.zgame.zgame.presenter

import com.google.firebase.database.*
import com.zgame.zgame.activity.SignUp3Activity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.SignUpContract
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant.DbName
import com.zgame.zgame.utils.Constant.uniqueName
import java.util.regex.Pattern


class SignUpPresenter (view: SignUpContract.SignUpView) : SignUpContract.SignUpPresenter {

    private var databaseRef: DatabaseReference? = null

    private val context = view as SignUp3Activity

    override fun doSignUp(signUpModel: SignUpModel) {

        context.mAuth.createUserWithEmailAndPassword(signUpModel.email!!, signUpModel.password!!)
            .addOnCompleteListener(context){ it ->
                if(it.isSuccessful){
                    FirebaseDatabase.getInstance().getReference(DbName)
                        .child(signUpModel.userName!!)
                        .setValue(signUpModel).addOnCompleteListener {
                            if (it.isSuccessful) {
                                PreferanceRepository.setString(uniqueName, signUpModel.userName!!)
                                context.registerDone()
                            } else {
                                context.registerNotDone(it.exception?.message)
                            }
                        }
                }else{
                    context.registerNotDone(it.exception?.message)
                }

        }
    }

    override fun checkUserName(uniqueName: String) {
        databaseRef = FirebaseDatabase.getInstance().reference.child(DbName).child(uniqueName)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) { //create new user
                    context.uniqueNameCorrect()
                }else{
                    context.uniqueNameInCorrect()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                context.databaseError(databaseError.message)
            }
        }
        databaseRef?.addListenerForSingleValueEvent(eventListener)
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