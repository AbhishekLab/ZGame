package com.zgame.zgame.contract

class  UserProfileContract{
    interface UserProfileView{
        fun fetchSuccessfully(userGalleryImages: ArrayList<String>)
        fun error(message:String)
    }

    interface UserProfilePresenter{
        fun getUserImages(uniqueName:String?)
    }
}