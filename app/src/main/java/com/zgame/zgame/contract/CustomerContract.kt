package com.zgame.zgame.contract

import com.google.firebase.database.DataSnapshot

class CustomerContract {

    interface CustomerView{
        fun getCustomerList(p0: DataSnapshot)
        fun getNullValue(message : String)
    }
    interface CustomerPresenter{
        fun customerList()
    }
}