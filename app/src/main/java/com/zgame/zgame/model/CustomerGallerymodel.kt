package com.zgame.zgame.model

data class CustomerGalleryModel(var name : String? , var age : Int?, var location : String?)

class CustomerResponse{
    val  response : ArrayList<CustomerData> ? = ArrayList()
}

class CustomerData{
    val age : String? = null
    var id : String? = null
    val image : String? = null
    val location : String? = null
    val name : String? = null

}