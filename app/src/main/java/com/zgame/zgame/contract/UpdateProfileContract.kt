package com.zgame.zgame.contract

import com.zgame.zgame.model.UpdateProfileModel

class UpdateProfileContract {

    interface UpdateProfileView{
        fun error(message : String)
        fun updateSuccessfully(update: String)
        fun setUserProfileData(response : UpdateProfileModel)
    }
    interface UpdateProfilePresenter{
        fun updateProfile(model : UpdateProfileModel)
        fun getUserProfileData()
    }
}