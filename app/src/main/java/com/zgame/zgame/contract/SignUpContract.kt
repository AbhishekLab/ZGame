package com.zgame.zgame.contract

import android.net.Uri
import com.zgame.zgame.model.SignUpModel

class SignUpContract {
    interface SignUpView {
        fun registerDone()
        fun registerNotDone(message: String?)
        fun uniqueNameCorrect()
        fun uniqueNameInCorrect()
        fun databaseError(message:String)

    }

    interface SignUpPresenter {
        fun doSignUp(
            signUpModel: SignUpModel,
            profilePhoto: Uri?
        )
        fun checkUserName(uniqueName:String)
    }

}