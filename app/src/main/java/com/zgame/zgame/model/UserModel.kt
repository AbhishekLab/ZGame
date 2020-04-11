package com.zgame.zgame.model

class UserRequest{
    var id : String = ""
    var firstName: String = ""
    var lastName : String = ""
    var email : String = ""
    var password : String = ""
    var number : String = ""
    var userContactList : HashMap<String, String> = HashMap()
    var wink : HashMap<String,String> = HashMap()
    var message : HashMap<String,String> = HashMap()
    var like : HashMap<String,String> = HashMap()
    var comment : HashMap<String,String> = HashMap()
    var mail : HashMap<String,String> = HashMap()
}

class UserResponse {
    val id : String = ""
    val firstName : String = ""
    val lastName : String = ""
    val email : String = ""
    val password : String = ""
    val mobileNumber : String = ""

}