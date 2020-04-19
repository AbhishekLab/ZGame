package com.zgame.zgame.contract


class LoginContract {
    interface LoginView{
        fun loginSuccess()
        fun loginFailed(message:String)
    }
    interface LoginPresenter{
        fun dialogLogin(email: String, password: String)
    }
}