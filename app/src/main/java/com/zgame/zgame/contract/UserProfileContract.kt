package com.zgame.zgame.contract

import com.zgame.zgame.model.ImageObj

class  UserProfileContract{
    interface UserProfileView{
        fun fetchSuccessfully(userGalleryImages: ImageObj)
        fun error(message:String)
    }

    interface UserProfilePresenter{
        fun getUserImages(uniqueName:String?)
    }
}