package com.zgame.zgame.contract

import com.zgame.zgame.model.PostModel
import com.zgame.zgame.model.UpdateProfileModel

class CustomerDetailContract {
    interface CustomerDetailView{
        fun getCustomerDetail(userProfileDetail: UpdateProfileModel)
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
        fun customerDetail(userName: String)
        fun wink(myUniqueName:String)
        fun winkRemove(myUniqueName:String)
        fun follow(follow: String?, follower: String)
        fun unFollow(follow: String?, follower: String)
        fun getSelectedUserImage(uniqueName:String)
    }
}