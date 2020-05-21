package com.zgame.zgame.contract

import com.zgame.zgame.model.PostModel

class  UserProfileContract{
    interface UserProfileView{
        fun fetchSuccessfully(userGalleryImages: PostModel)
        fun error(message:String)
    }

    interface UserProfilePresenter{
        fun getUserImages(uniqueName:String?)
    }
}