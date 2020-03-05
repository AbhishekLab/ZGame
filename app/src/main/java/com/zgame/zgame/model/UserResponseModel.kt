package com.zgame.zgame.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponseModel {
    @SerializedName("tokenInfo")
    @Expose
     val tokenInfo: TokenInfo? = null
    @SerializedName("userInfo")
    @Expose
     val userInfo: UserInfo? = null
    @SerializedName("error")
    @Expose
    var error: ErrorInfo? = null
}

class UserInfo{
    @SerializedName("name")
    @Expose
     var name: String? = null
    /*@SerializedName("designation")
    @Expose
     var designation: String? = null*/
    @SerializedName("email")
    @Expose
     var email: String? = null
    @SerializedName("mobileNo")
    @Expose
     var mobileNo: String? = null
    @SerializedName("profilePicUrl")
    @Expose
    var profilePicUrl: String? = null
    @SerializedName("WeeklyTrade")
    @Expose
    var weeklyTrade: Boolean? = null
    @SerializedName("MeetingUpdates")
    @Expose
    var meetingUpdates: Boolean? = null
    @SerializedName("CompanyResearchNote")
    @Expose
    var companyResearchNote: Boolean? = null
    @SerializedName("PeerResearchNote")
    @Expose
    var peerResearchNote: Boolean? = null
    @SerializedName("PostMeetingNotes")
    @Expose
    var postMeetingNotes: Boolean? = null
}

class TokenInfo{
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("userId")
    @Expose
    var userId: Int? = null
    @SerializedName("sessionToken")
    @Expose
    var sessionToken: String? = null
}

//for handling Errors
class ErrorInfo{
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null
}