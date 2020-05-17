package com.zgame.zgame.contract

import android.net.Uri

class PostContract {
    interface PostView{
        fun postSuccess()
        fun postFailed(message:String)
    }
    interface PostPresenter{
        fun postImage(imageFile: Uri?, userUniqueName: String?)
    }

}