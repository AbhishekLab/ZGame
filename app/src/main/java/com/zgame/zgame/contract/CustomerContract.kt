package com.zgame.zgame.contract

import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.model.CircleData
import com.zgame.zgame.model.SignUpModel

class CustomerContract {

    interface CustomerView{
        fun getCustomerRandomList(p0: DataSnapshot)
        fun getUsersFilterList(userFilterList: ArrayList<SignUpModel>?)
        fun getNullValue(message : String)
        fun setContactImages(images: ArrayList<String>)
        fun getCircleProfileData(circleProfile: ArrayList<CircleData>?)
    }
    interface CustomerPresenter{
        fun customerRandomList()
        fun usersFilterList()
        fun getContactRandomImages()
        fun circleProfile()
    }
}