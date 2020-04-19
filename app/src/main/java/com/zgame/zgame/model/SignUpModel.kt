package com.zgame.zgame.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class SeekingModule(var isChecked: Boolean = false, var name: String? = null): Parcelable

@Parcelize
class AgeModule(var isChecked: Boolean = false, var name: String? = null): Parcelable

@Parcelize
class GenderModule(var isChecked: Boolean = false, var name: String? = null): Parcelable

class SignUpModel {
    var gender : ArrayList<String>? = ArrayList()
    var ageRange : ArrayList<String>? = ArrayList()
    var male : String? = null
    var female : String? = null
    var coupleFM : String? = null
    var coupleFF : String? = null
    var coupleMM : String? = null
    var age : String? = null
    var height : String? = null
    var country : String? = null
    var state : String? = null
    var email : String? = null
    var userName : String? = null
    var password : String? = null
    var profilePic : String? = null
}