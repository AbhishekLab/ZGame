package com.zgame.zgame.repositories

import com.zgame.zgame.model.UserResponseModel
import com.zgame.zgame.network.ApiInterface
import retrofit2.Call

open class APIRepository {

    private val apiInterface: ApiInterface = ApiInterface.create()

    fun login(email: String, password:String,companyId:Int): Call<UserResponseModel> = apiInterface.login(email,password,companyId)

}

