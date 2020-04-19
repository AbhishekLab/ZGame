package com.zgame.zgame.contract

import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.model.SignUpModel

class CustomerContract {

    interface CustomerView{
        fun getCustomerRandomList(p0: DataSnapshot)
        fun getUsersFilterList(userFilterList: ArrayList<SignUpModel>?)
        fun getNullValue(message : String)
    }
    interface CustomerPresenter{
        fun customerRandomList()
        fun usersFilterList()
    }
}