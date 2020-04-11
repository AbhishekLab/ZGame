package com.zgame.zgame.contract

import com.google.firebase.database.DataSnapshot

class CustomerDetailContract {
    interface CustomerDetailView{
        fun getCustomerDetail(p0: DataSnapshot)
        fun loginSuccess()
        fun loginFailed(message:String)
        fun winkAdded()
    }
    interface CustomerDetailPresenter{
        fun dialogLogin(email: String, password: String)
        fun customerDetail(id: String)
        fun wink()
    }
}