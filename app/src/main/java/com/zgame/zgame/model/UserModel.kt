package com.zgame.zgame.model

class UserRequest(val firstName: String, val lastName : String, val email : String, val password : String, val number : String, val userContactList : HashMap<String, String>)
class UserResponse {
    val firstName : String = ""
    val lastName : String = ""
    val email : String = ""
    val password : String = ""
    val mobileNumber : String = ""

}