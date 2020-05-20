package com.zgame.zgame.contract

import com.google.firebase.database.DataSnapshot
import com.zgame.zgame.model.PostModel

class CustomerDetailContract {
    interface CustomerDetailView{
        fun getCustomerDetail(p0: DataSnapshot)
        fun loginSuccess()
        fun loginFailed(message:String)
        fun winkAdded()
        fun winkRemove()
        fun followDone()
        fun unFollowDone()
        fun error(message: String)
        fun setSelectedUserImage(userImage: PostModel)
    }
    interface CustomerDetailPresenter{
        fun dialogLogin(email: String, password: String)
        fun customerDetail(id: String)
        fun wink(myUniqueName:String)
        fun winkRemove(myUniqueName:String)
        fun follow(follow: String?, follower: String)
        fun unFollow(follow: String?, follower: String)
        fun getSelectedUserImage(uniqueName:String)
    }
}